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

    override fun hideWtfDivider(duration: Long) {
        animManager.hideWtfDivider(duration)
    }

    override fun btnStartClicked() {
        // starting a new activity has some lag to it, thus durations below are not equal
        animManager.hideWtfDivider(600)
        view.startGameActivityWithDelay(400)
    }
}