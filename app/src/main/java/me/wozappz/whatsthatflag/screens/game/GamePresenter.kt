package me.wozappz.whatsthatflag.screens.game

import com.squareup.picasso.Callback
import me.wozappz.whatsthatflag.data.Model

/**
 * Created by olq on 20.11.17.
 */
class GamePresenter(private val view: GameScreenContract.View,
                    private val model: Model)
    : GameScreenContract.Presenter {


    private var score = 0
    private var currentFlagId = 0
    private var amountOfLoadedCountries = 0


    override fun start() {
        score = 0
        currentFlagId = 0

        view.showScore(score)

        amountOfLoadedCountries = model.flagList.size

        view.setButtonsClickability(false)
        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    private fun downloadImg(id: Int) {
        val imgUrl = model.flagList[id].second

        view.loadImg(imgUrl, object : Callback {
            override fun onSuccess() {
                renameBtns(currentFlagId)
                view.startAnswerTimer()
                view.setButtonsClickability(true)
            }

            override fun onError() {
                view.displayMessageErrorLoadNextFlag()
                goToNextFlag()
            }
        })
    }

    override fun redownloadImg(goToNext: Boolean) {
        if (!goToNext) {
            downloadImg(currentFlagId)

        } else {
            val currentFlag = model.flagList[currentFlagId].first
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