package me.wozappz.whatsthatflag.screens.game

import com.squareup.picasso.Callback
import me.wozappz.whatsthatflag.data.Model
import javax.inject.Inject

/**
 * Created by olq on 20.11.17.
 */
class GamePresenter @Inject constructor(
        private val view: GameScreenContract.View,
        private val model: Model)
    : GameScreenContract.Presenter {


    private var score = 0
    private var currentFlagId = 0
    private var amountOfLoadedCountries = 0
    private var amountOfNetworkErrors = 0


    override fun start() {
        score = 0
        currentFlagId = 0
        amountOfNetworkErrors = 0

        view.showScore(score)

        amountOfLoadedCountries = model.flagList.size

        view.setButtonsClickability(false)
        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    private fun downloadImg(id: Int) {
        val imgUrl = model.flagList[id].second

        view.loadImg(imgUrl, callback = object : Callback {
            override fun onSuccess() {
                loadImgSuccess()
            }

            override fun onError() {
                view.loadImg(imgUrl, false, object : Callback {
                    override fun onSuccess() {
                        loadImgSuccess()
                    }

                    override fun onError() {
                        amountOfNetworkErrors++

                        if (amountOfNetworkErrors < 3) {
                            view.displayMessageErrorLoadNextFlag()
//                        Log.d("loadImgError", "${model.flagList[id].first}, ${model.flagList[id].second}")
                            goToNextFlag()
                        } else {
                            view.showNoConnectionAlert()
                        }
                    }
                })
            }
        })
    }

    private fun loadImgSuccess() {
        renameBtns(currentFlagId)
        view.startAnswerTimer()
        view.setButtonsClickability(true)
    }

    override fun redownloadImg(goToNext: Boolean) {
        if (!goToNext) {
            downloadImg(currentFlagId)

        } else {
            val currentFlag = model.flagList[currentFlagId].first
            view.displayMessageFlagSkipped(currentFlag)
//            Log.d("loadImgError", "[Flag Skipped]  ${model.flagList[currentFlagId].first}, ${model.flagList[currentFlagId].second}")
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

        val correctCountry = model.flagList[currentFlagId].first
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

        } else {
            view.showRemainingQuestions(0)
            view.showSummaryDialog(score, amountOfLoadedCountries)
        }
    }

    override fun answerTimerFinished() {
        val correctCountry = model.flagList[currentFlagId].first

        view.animateCorrectAnswer(correctCountry, false)
        view.setButtonsClickability(false)
    }


    override fun btnWTFclicked() {
        view.stopAnswerTimer()

        val currentFlag = model.flagList[currentFlagId].first
        val url = model.getURLFromName(currentFlag)
        view.displayFlagInfoInBrowser(url)
    }

    override fun backButtonClicked() {
        view.showBackToMenuDialog()
    }
}