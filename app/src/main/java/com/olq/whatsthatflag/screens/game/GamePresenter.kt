package com.olq.whatsthatflag.screens.game

import com.olq.whatsthatflag.data.Model
import com.olq.whatsthatflag.screens.menu.MenuActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by olq on 20.11.17.
 */
class GamePresenter(private val view: GameScreenContract.View,
                    private val model: Model)
    : GameScreenContract.Presenter {


    var score = 0
    var currentFlagId = 0
    var amountOfLoadedCountries = 0


    override fun start(gameData: Pair<MenuActivity.CONTINENT, Int>) {
        score = 0
        currentFlagId = 0

        view.showScore(score)

        model.selectFlags(gameData)
        amountOfLoadedCountries = model.flagList.size


        if (gameData.first == MenuActivity.CONTINENT.OCEANIA && gameData.second == 40) {
            view.displayMessage("There are only $amountOfLoadedCountries countries in Oceania")
        }

        view.setButtonsClickability(false)
        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    private fun downloadImg(id: Int) {
        view.showProgressBar()

        doAsync {
            val country = model.flagList[id]
            val imgUrl = model.getImgUrl(getURLFromName(country))

            uiThread {
                when (imgUrl) {
                    null -> {
                        if (view.isConnectedToInternet()) {
                            downloadImg(id)
                            view.displayMessage("Reattempting download")
                        } else {
                            view.showNoConnectionAlert()
                        }
                    }

                    else -> {
                        view.loadImg(imgUrl)

                        view.hideProgressBar()
                        view.setButtonsClickability(true)

                        view.startAnswerTimer()
                    }
                }
            }
        }
    }

    override fun redownloadImg(goToNext: Boolean) {
        if (!goToNext) {
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            manageFlagId()
        }
    }

    private fun renameBtns(id: Int) {
        val btnNames = model.getButtonNames(id)
        view.renameButtons(btnNames)

        view.showRemainingQuestions(amountOfLoadedCountries - (currentFlagId + 1))
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
        manageFlagId()
    }

    private fun manageFlagId() {
        if (currentFlagId < amountOfLoadedCountries - 1) {
            currentFlagId++
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            view.showSummaryDialog(score, amountOfLoadedCountries)
        }
    }

    override fun answerTimerFinished() {
        val correctCountry = model.flagList[currentFlagId]

        view.animateCorrectAnswer(correctCountry, false)
        view.setButtonsClickability(false)
    }

    private fun getURLFromName(countryName: String): String {
        val validCountryName: String = countryName.replace("[ ]".toRegex(), "_")
        return "https://en.wikipedia.org/wiki/$validCountryName"
    }
}