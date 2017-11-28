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
import android.graphics.Color
import android.os.Handler

class MenuActivity : AppCompatActivity() {

    enum class CONTINENT {
        GLOBAL,
        EUROPE,
        ASIA,
        AMERICAS,
        AFRICA,
        OCEANIA
    }

    private val animManager by lazy { AnimationManager(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.setBackgroundColor(Color.BLACK)

        mSeekBarText.text = getString(R.string.countries_amount, "20")
        setSeekBarListener()
        setStartBtnListener()

        // Make one of radio buttons selected
        mRadioGlobal.callOnClick()

        animManager.setupGlobeAnimation()
        animManager.animateViewsAlpha(0.2f, 0)
    }

    override fun onResume() {
        super.onResume()

        if (mGlobeGif.alpha != 1f) {
            animManager.hideWtfDivider()
            animManager.showWtfDivider(800, 200)
        }
    }


    fun onGlobeClicked(view: View) {
        animManager.animateViewsAlpha(1f, 1200)
        animManager.compoundGlobeAnimation()
        animManager.animateBackgroundColor()
    }


    private fun setStartBtnListener() {
        mStartBtn.setOnClickListener {
            animManager.hideWtfDivider(600)

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
