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

        downloadImg(currentFlagId)
        renameBtns(currentFlagId)
    }

    fun downloadImg(id: Int) {
        view.showProgressBar()

        doAsync {
            val country = model.flagList[id]
            val imgUrl = model.getImgUrl(getURLFromName(country))

            uiThread {
                if (imgUrl != null) {
                    view.loadImg(imgUrl)
                }

                view.hideProgressBar()
            }
        }
    }

    fun renameBtns(id: Int) {
        val btnNames = model.getButtonNames(id)
        view.renameButtons(btnNames)
    }


    override fun answerBtnClicked(countryName: String) {
        if (countryName == model.flagList[currentFlagId]) {
            score++
            view.showScore(score)
        }


        if (currentFlagId < amountOfLoadedCountries - 1) {
            currentFlagId++
            downloadImg(currentFlagId)
            renameBtns(currentFlagId)

        } else {
            // TODO: consider -> display SummaryScreen, remove view.showSummaryDialog()
            view.showSummaryDialog(score, amountOfLoadedCountries)
        }
    }


    fun getURLFromName(countryName: String): String{
        return "https://en.wikipedia.org/wiki/$countryName"
    }
}