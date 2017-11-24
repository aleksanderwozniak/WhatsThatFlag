package com.olq.whatsthatflag.screens.game

import com.olq.whatsthatflag.screens.BaseView
import com.olq.whatsthatflag.screens.menu.MenuActivity

/**
 * Created by olq on 20.11.17.
 */
interface GameScreenContract {

    interface View: BaseView<Presenter> {
        fun loadImg(currentUrl: String)
        fun renameButtons(btnNames: List<String>)
        fun showScore(score: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun showSummaryDialog(score: Int, totalFlagAmount: Int)
        fun displayMessage(msg: String)
        fun animateCorrectAnswer(btnName: String)
        fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String)
        fun setButtonsClickability(enabled: Boolean)
        fun isConnectedToInternet(): Boolean
        fun showNoConnectionAlert()
    }


    interface Presenter {
        fun start(gameData: Pair<MenuActivity.CONTINENT, Int>)
        fun answerBtnClicked(selectedCountry: String)
        fun timerFinished()
        fun refreshConnection()
    }
}