package com.olq.whatsthatflag.screens.menu


/**
 * Created by olq on 10.12.17.
 */
class MenuPresenter(private val view: MenuScreenContract.View)
    : MenuScreenContract.Presenter {

    private val animManager = MenuAnimationManager(view as MenuActivity)

    override fun start() {
        animManager.setupGlobeAnimation()
        animManager.animateViewsAlpha(0.2f, 0)
    }

    override fun restartWtfDividerAnimation() {
        animManager.hideWtfDivider()
        animManager.showWtfDivider(800, 200)
    }

    override fun startGlobeAnimation() {
        animManager.animateViewsAlpha(1f, 1200)
        animManager.compoundGlobeAnimation()
        animManager.animateBackgroundColor()
    }


    override fun btnInfoClicked() {
        view.showAppInfo()
    }

    override fun btnSourceClicked() {
        view.showGitHubSourceInBrowser()
    }

    override fun btnStartClicked() {
        val amount = calculateAmountOfCountries(view.getFlagSeekbarProgress())
        val continent = view.getSelectedContinent()

        view.setStartButtonClickability(false)

        // starting a new activity has some lag to it, thus durations below are not equal
        animManager.hideWtfDivider(600)
        view.startGameActivityWithDelay(amount, continent, 400)
    }

    private fun calculateAmountOfCountries(seekbarProgress: Int): Int {
        when (seekbarProgress) {
            0 -> return 5
            1 -> return 10
            2 -> return 20
            3 -> return 40
            4 -> return -1 // ALL = -1

            else -> { return -1 }
        }
    }

    override fun flagSeekbarProgressChanged(progress: Int) {
        val amount = calculateAmountOfCountries(progress)

        if (amount == -1) {
            view.showFlagSeekbarLabelAll()
        } else {
            view.showFlagSeekbarLabel(amount)
        }
    }
}