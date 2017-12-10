package com.olq.whatsthatflag.screens.game

import com.olq.whatsthatflag.data.Model
import com.olq.whatsthatflag.screens.menu.CONTINENT
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by olq on 20.11.17.
 */
class GamePresenter(private val view: GameScreenContract.View,
                    private val model: Model)
    : GameScreenContract.Presenter {


    var score = 0
    var currentFlagId = 0
    var amountOfLoadedCountries = 0


    override fun start(gameData: Pair<CONTINENT, Int>) {
        score = 0
        currentFlagId = 0

        view.showScore(score)

        model.selectFlags(gameData)
        amountOfLoadedCountries = model.flagList.size


        if (gameData.first == CONTINENT.OCEANIA && gameData.second == 40) {
            view.displayMessage("There are only $amountOfLoadedCountries countries in Oceania")
        }

        view.setButtonsClickability(false)
        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    private fun downloadImg(id: Int) {
        view.showProgressBar()

        val viewRef: Ref<GameScreenContract.View> = view.asReference()


        async(UI) {
            val country = bg { model.flagList[id] }
            val imgUrl = bg { model.getImgUrl(getURLFromName(country.getCompleted())) }

            when (imgUrl.await()) {
                null -> {
                    if (viewRef.invoke().isConnectedToInternet()) {
                        downloadImg(id)
                        viewRef.invoke().displayMessage("Reattempting download")
                    } else {
                        viewRef.invoke().showNoConnectionAlert()
                    }
                }

                else -> {
                    viewRef.invoke().loadImg(imgUrl.getCompleted() as String)

                    viewRef.invoke().hideProgressBar()
                    viewRef.invoke().setButtonsClickability(true)

                    viewRef.invoke().startAnswerTimer()
                }
            }
        }
    }

    override fun redownloadImg(goToNext: Boolean) {
        if (!goToNext) {
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            view.displayMessage("Skipped a flag")
            goToNextFlag()
        }
    }

    private fun renameBtns(id: Int) {
        val btnNames = model.getButtonNames(id)
        view.renameButtons(btnNames)

        view.showRemainingQuestions(amountOfLoadedCountries - currentFlagId)
    }


    override fun answerBtnClicked(selectedCountry: String) {
        view.stopAnswerTimer()

        val correctCountry = model.flagList[currentFlagId]
        val success = isAnswerCorrect(selectedCountry, correctCountry)

        if (!success) {
            view.animateWrongAnswer(selectedCountry, correctCountry)
        } else {
            view.animateCorrectAnswer(correctCountry)
        }

        view.setButtonsClickability(false)
    }

    private fun isAnswerCorrect(selectedCountry: String, correctCountry: String): Boolean {
        if (selectedCountry == correctCountry) {
            score++
            view.showScore(score)

            return true
        }

        return false
    }

    override fun animationTimerFinished() {
        goToNextFlag()
    }

    private fun goToNextFlag() {
        if (currentFlagId < amountOfLoadedCountries - 1) {
            currentFlagId++
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            view.showRemainingQuestions(0)
            view.showSummaryDialog(score, amountOfLoadedCountries)
        }
    }

    override fun answerTimerFinished() {
        val correctCountry = model.flagList[currentFlagId]

        view.animateCorrectAnswer(correctCountry, false)
        view.setButtonsClickability(false)
    }


    override fun btnWTFclicked() {
        view.stopAnswerTimer()

        val url = getURLFromName(model.flagList[currentFlagId])
        view.displayFlagInfoInBrowser(url)
    }

    private fun getURLFromName(countryName: String): String {
        val validCountryName: String = countryName.replace("[ ]".toRegex(), "_")
        return "https://en.wikipedia.org/wiki/$validCountryName"
    }
}