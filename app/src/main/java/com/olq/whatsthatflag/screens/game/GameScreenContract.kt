package com.olq.whatsthatflag.screens.game

import com.olq.whatsthatflag.screens.BaseView
import com.olq.whatsthatflag.screens.menu.CONTINENT
import com.squareup.picasso.Callback

/**
 * Created by olq on 20.11.17.
 */
interface GameScreenContract {

    interface View: BaseView<Presenter> {
        fun loadImg(currentUrl: String, callback: Callback)
        fun renameButtons(btnNames: List<String>)
        fun showScore(score: Int)
        fun showRemainingQuestions(amount: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun showSummaryDialog(score: Int, totalFlagAmount: Int)
        fun displayMessageOceaniaMaxFlags(amount: Int)
        fun displayMessageErrorLoadNextFlag()
        fun displayMessageReloadImg()
        fun displayMessageFlagSkipped(flagName: String)
        fun animateCorrectAnswer(btnName: String, staticAnimation: Boolean = true)
        fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String)
        fun setButtonsClickability(enabled: Boolean)
        fun isConnectedToInternet(): Boolean
        fun showNoConnectionAlert()
        fun startAnswerTimer()
        fun stopAnswerTimer()
        fun displayFlagInfoInBrowser(url: String)
        fun showBackToMenuDialog()
    }


    interface Presenter {
        fun start(gameData: Pair<CONTINENT, Int>)
        fun answerBtnClicked(selectedCountry: String)
        fun animationTimerFinished()
        fun redownloadImg(goToNext: Boolean = false)
        fun answerTimerFinished()
        fun btnWTFclicked()
        fun backButtonClicked()
    }
}