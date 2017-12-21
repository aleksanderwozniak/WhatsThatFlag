package me.wozappz.whatsthatflag.screens.game

import com.squareup.picasso.Callback

/**
 * Created by olq on 20.11.17.
 */
interface GameScreenContract {

    interface View: me.wozappz.whatsthatflag.screens.BaseView<me.wozappz.whatsthatflag.screens.game.GameScreenContract.Presenter> {
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
        fun start(gameData: Pair<me.wozappz.whatsthatflag.screens.menu.CONTINENT, Int>)
        fun answerBtnClicked(selectedCountry: String)
        fun animationTimerFinished()
        fun redownloadImg(goToNext: Boolean = false)
        fun answerTimerFinished()
        fun btnWTFclicked()
        fun backButtonClicked()
    }
}