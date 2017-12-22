package me.wozappz.whatsthatflag.screens.menu

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_menu.*
import me.wozappz.whatsthatflag.R


/**
 * Created by olq on 28.11.17.
 */
class MenuAnimationManager(private val menuActivity: MenuActivity) {


    fun animateViewsAlpha(alphaValue: Float, duration: Long) {
        val views = listOf(menuActivity.mWtfLogo, menuActivity.mWtfLogoText,
                menuActivity.mSeekBarText, menuActivity.mCountriesSeekBar,
                menuActivity.mStartBtn, menuActivity.mInfoBtn)

        val enabled = (alphaValue == 1f)

        views.forEach {
            it.isEnabled = enabled
            it.animate()
                    .alpha(alphaValue)
                    .setDuration(duration)
                    .start()
        }
    }

    fun setupGlobeAnimation() {
        menuActivity.mRadioGroupContinents.animate()
                .alpha(0f)
                .scaleX(0.01f)
                .scaleY(0.01f)
                .setDuration(0)
                .start()

        hideWtfDivider()
    }

    fun showWtfDivider(duration: Long = 0, delay: Long = 0) {
        animateWtfDivider(1f, 1f, duration, delay)
    }

    fun hideWtfDivider(duration: Long = 0, delay: Long = 0) {
        animateWtfDivider(0f, 2f, duration, delay)
    }

    private fun animateWtfDivider(alphaValue: Float, scaleValue: Float,
                                  duration: Long, delay: Long) {
        menuActivity.mWtfDividerView.animate()
                .setStartDelay(delay)
                .alpha(alphaValue)
                .scaleX(scaleValue)
                .setDuration(duration)
                .start()
    }

    fun compoundGlobeAnimation() {
        menuActivity.mGlobeGif.isClickable = false

        menuActivity.mGlobeGif.animate()
                .alpha(0f)
                .scaleX(0.01f)
                .scaleY(0.01f)
                .setDuration(1200)
                .start()


        val customOffset = 24f
        val totalOffsetY = (menuActivity.mRadioGroupContinents.height / 2) + customOffset

        menuActivity.mRadioGroupContinents.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationYBy(totalOffsetY / 2)
                .setDuration(1200)
                .start()

        menuActivity.mContinentSelTextView.animate()
                .setStartDelay(800)
                .translationYBy(-totalOffsetY / 2)
                .setDuration(600)
                .start()


        showWtfDivider(800, 1200)
    }

    fun animateBackgroundColor() {
        val colorFrom = ContextCompat.getColor(menuActivity, R.color.blackPure)
        val colorTo = ContextCompat.getColor(menuActivity, R.color.blackSubtle)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.startDelay = 1250
        colorAnimation.duration = 700
        colorAnimation.addUpdateListener { animator -> menuActivity.window.decorView.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }
}