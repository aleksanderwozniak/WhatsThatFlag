package me.wozappz.whatsthatflag.screens.menu

/**
 * Created by olq on 10.12.17.
 */
interface MenuScreenContract {

    interface View: me.wozappz.whatsthatflag.screens.BaseView<me.wozappz.whatsthatflag.screens.menu.MenuScreenContract.Presenter> {
        fun startGameActivityWithDelay(amount: Int, selectedContinent: me.wozappz.whatsthatflag.screens.menu.CONTINENT, duration: Long)
        fun getSelectedContinent(): me.wozappz.whatsthatflag.screens.menu.CONTINENT
        fun getFlagSeekbarProgress(): Int
        fun showFlagSeekbarLabel(amount: Int)
        fun showFlagSeekbarLabelAll()
        fun showAppInfo()
        fun showGitHubSourceInBrowser()
        fun setStartButtonClickability(isClickable: Boolean)
    }

    interface Presenter {
        fun start()
        fun restartWtfDividerAnimation()
        fun startGlobeAnimation()
        fun btnStartClicked()
        fun flagSeekbarProgressChanged(progress: Int)
        fun btnInfoClicked()
        fun btnSourceClicked()
    }
}