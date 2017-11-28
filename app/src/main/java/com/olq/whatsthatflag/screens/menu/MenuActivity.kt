package com.olq.whatsthatflag.screens.menu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.olq.whatsthatflag.screens.game.GameActivity
import com.olq.whatsthatflag.R
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.radio_group_table_layout.*
import org.jetbrains.anko.startActivity
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.support.v4.content.ContextCompat

class MenuActivity : AppCompatActivity() {

    enum class CONTINENT {
        GLOBAL,
        EUROPE,
        ASIA,
        AMERICAS,
        AFRICA,
        OCEANIA
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.setBackgroundColor(Color.BLACK)

        mSeekBarText.text = getString(R.string.countries_amount, "20")
        setSeekBarListener()
        setStartBtnListener()

        // Make one of radio buttons selected
        mRadioGlobal.callOnClick()

        setupGlobeAnimation()
        animateViewsAlpha(0.2f, 0)
    }

    override fun onResume() {
        super.onResume()

        if (mGlobeGif.alpha != 1f) {
            showWtfDivider(800, 200)
        }
    }

    private fun animateViewsAlpha(alphaValue: Float, duration: Long) {
        val views = listOf(mWtfLogo, mWtfLogoText, mSeekBarText, mCountriesSeekBar, mStartBtn)

        val enabled = (alphaValue == 1f)

        views.forEach {
            it.isEnabled = enabled
            it.animate()
                    .alpha(alphaValue)
                    .setDuration(duration)
                    .start()
        }
    }

    private fun setupGlobeAnimation() {
        mRadioGroupContinents.animate()
                .alpha(0f)
                .scaleX(0.01f)
                .scaleY(0.01f)
                .setDuration(0)
                .start()

        hideWtfDivider()
    }

    private fun showWtfDivider(duration: Long = 0, delay: Long = 0) {
        animateWtfDivider(1f, 1f, duration, delay)
    }

    private fun hideWtfDivider(duration: Long = 0, delay: Long = 0) {
        animateWtfDivider(0f, 2f, duration, delay)
    }

    private fun animateWtfDivider(alphaValue: Float, scaleValue: Float,
                                  duration: Long, delay: Long) {
        mWtfDividerView.animate()
                .setStartDelay(delay)
                .alpha(alphaValue)
                .scaleX(scaleValue)
                .setDuration(duration)
                .start()
    }

    fun onGlobeClicked(view: View) {
        animateViewsAlpha(1f, 1200)
        compoundGlobeAnimation()
        animateBackgroundColor()
    }

    private fun compoundGlobeAnimation() {
        mGlobeGif.animate()
                .alpha(0f)
                .scaleX(0.01f)
                .scaleY(0.01f)
                .setDuration(1200)
                .start()


        val customOffset = 24f
        val totalOffsetY = (mRadioGroupContinents.height / 2) + customOffset

        mRadioGroupContinents.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationYBy(totalOffsetY / 2)
                .setDuration(1200)
                .start()

        mContinentSelTextView.animate()
                .setStartDelay(800)
                .translationYBy(-totalOffsetY / 2)
                .setDuration(600)
                .start()


        showWtfDivider(800, 1200)
    }

    private fun animateBackgroundColor() {
        val colorFrom = ContextCompat.getColor(this, R.color.blackPure)
        val colorTo = ContextCompat.getColor(this, R.color.blackSubtle)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.startDelay = 1250
        colorAnimation.duration = 700
        colorAnimation.addUpdateListener { animator -> window.decorView.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    private fun setStartBtnListener() {
        mStartBtn.setOnClickListener {
            hideWtfDivider(600)

            Handler().postDelayed({
                startGameActivity()
            }, 400)
        }
    }

    private fun startGameActivity() {
        val amount = calculateAmountOfCountries(mCountriesSeekBar.progress)
        val selectedContinent = getSelectedContinent((mRadioGroupContinents as RadioGroupTableLayout).getCheckedRadioButtonId())

        startActivity<GameActivity>(
                "AMOUNT_OF_COUNTRIES" to amount,
                "SELECTED_CONTINENT" to selectedContinent)

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }


    override fun onBackPressed() {
        // Prevents going back to StartScreen
        moveTaskToBack(true)
    }


    private fun calculateAmountOfCountries(progress: Int): Int {
        when (progress) {
            0 -> return 5
            1 -> return 10
            2 -> return 20
            3 -> return 40
            4 -> return -1 // ALL = -1

            else -> { return -1 }
        }
    }

    private fun getSelectedContinent(radioBtnId: Int): CONTINENT {
        when (radioBtnId) {
            mRadioGlobal.id -> return CONTINENT.GLOBAL
            mRadioEurope.id -> return CONTINENT.EUROPE
            mRadioAsia.id -> return CONTINENT.ASIA
            mRadioAmericas.id -> return CONTINENT.AMERICAS
            mRadioAfrica.id -> return CONTINENT.AFRICA
            mRadioOceania.id -> return CONTINENT.OCEANIA

            else -> {
                return CONTINENT.GLOBAL
            }
        }
    }


    private fun setSeekBarListener() {
        mCountriesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                val amount = calculateAmountOfCountries(p1)

                if (amount == -1) {
                    mSeekBarText.text = getString(R.string.countries_amount, getString(R.string.countries_all))
                } else {
                    mSeekBarText.text = getString(R.string.countries_amount, amount.toString())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}
