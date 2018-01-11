package me.wozappz.whatsthatflag.screens.menu

import me.wozappz.whatsthatflag.screens.BasePresenter
import me.wozappz.whatsthatflag.screens.BaseView

/**
 * Created by olq on 10.12.17.
 */
interface MenuScreenContract {

    interface View: BaseView<Presenter> {
        fun startGameActivityWithDelay(selectedContinent: CONTINENT, duration: Long)
        fun getSelectedContinent(): CONTINENT
        fun getFlagSeekbarProgress(): Int
        fun showFlagSeekbarLabel(amount: Int)
        fun showFlagSeekbarLabelAll()
        fun showAppInfo()
        fun showGitHubSourceInBrowser()
        fun setStartButtonClickability(isClickable: Boolean)
        fun displayMessageOceaniaMaxFlags(amount: Int)
        fun animateViewsAlpha(alphaValue: Float, duration: Long)
        fun setupGlobeAnimation()
        fun showWtfDivider(duration: Long = 0, delay: Long = 0)
        fun hideWtfDivider(duration: Long = 0, delay: Long = 0)
        fun runCompoundGlobeAnimation()
        fun animateBackgroundColor()
    }

    interface Presenter: BasePresenter {
        fun restartWtfDividerAnimation()
        fun startGlobeAnimation()
        fun btnStartClicked()
        fun flagSeekbarProgressChanged(progress: Int)
        fun btnInfoClicked()
        fun btnSourceClicked()
    }
}