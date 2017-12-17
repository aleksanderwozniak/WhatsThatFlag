package com.olq.whatsthatflag.screens.game

import com.olq.whatsthatflag.data.Model
import com.olq.whatsthatflag.screens.menu.CONTINENT
import com.squareup.picasso.Callback
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


    private var score = 0
    private var currentFlagId = 0
    private var amountOfLoadedCountries = 0


    override fun start(gameData: Pair<CONTINENT, Int>) {
        score = 0
        currentFlagId = 0

        view.showScore(score)

        model.selectFlags(gameData)
        amountOfLoadedCountries = model.flagList.size


        if (gameData.first == CONTINENT.OCEANIA && gameData.second == 40) {
            view.displayMessageOceaniaMaxFlags(amountOfLoadedCountries)
        }

        view.setButtonsClickability(false)
        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    private fun downloadImg(id: Int) {
        view.showProgressBar()

        val viewRef: Ref<GameScreenContract.View> = view.asReference()


        async(UI) {
            val country = model.flagList[id]
            val imgUrl = bg {
                val url = model.getURLFromName(country)
                model.getImgUrl(url)
            }

            when (imgUrl.await()) {
                null -> {
                    if (viewRef.invoke().isConnectedToInternet()) {
                        viewRef.invoke().hideProgressBar()
                        goToNextFlag()
                        viewRef.invoke().displayMessageErrorLoadNextFlag()
                    } else {
                        viewRef.invoke().showNoConnectionAlert()
                    }
                }

                else -> {
                    viewRef.invoke().loadImg(imgUrl.getCompleted() as String, object : Callback {
                        override fun onSuccess() {
                            view.startAnswerTimer()
                            view.hideProgressBar()
                            view.setButtonsClickability(true)
                        }

                        override fun onError() {
                            view.displayMessageReloadImg()
                            downloadImg(id)
                        }
                    })
                }
            }
        }
    }

    override fun redownloadImg(goToNext: Boolean) {
        if (!goToNext) {
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            val currentFlag = model.flagList[currentFlagId]
            view.displayMessageFlagSkipped(currentFlag)
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

        val currentFlag = model.flagList[currentFlagId]
        val url = model.getURLFromName(currentFlag)
        view.displayFlagInfoInBrowser(url)
    }

    override fun backButtonClicked() {
        view.showBackToMenuDialog()
    }
}