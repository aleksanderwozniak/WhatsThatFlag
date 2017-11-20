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

        myCountriesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                val amount = calculateAmountOfCountries(p1)

                if (amount == -1) {
                    mySeekBarText.text = "Countries: All"
                } else {
                    mySeekBarText.text = "Countries: $amount"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        // Make one of radio buttons selected
        radioGlobal.callOnClick()
    }

    fun calculateAmountOfCountries(progress: Int): Int {
        when (progress) {
            0 -> return 5
            1 -> return 10
            2 -> return 20
            3 -> return 40
            4 -> return -1

            else -> { return -1 }
        }
    }

    fun onStartBtnClick(view: View) {
        val amount = calculateAmountOfCountries(myCountriesSeekBar.progress)
        val selectedContinent = getSelectedContinent((radioGroupContinents as RadioGroupTableLayout).getCheckedRadioButtonId())

        startActivity<GameActivity>(
                "AMOUNT_OF_COUNTRIES" to amount,
                "SELECTED_CONTINENT" to selectedContinent)
    }

    fun getSelectedContinent(radioBtnId: Int): CONTINENT {
        when (radioBtnId) {
            radioGlobal.id -> return CONTINENT.GLOBAL
            radioEurope.id -> return CONTINENT.EUROPE
            radioAsia.id -> return CONTINENT.ASIA
            radioAmericas.id -> return CONTINENT.AMERICAS
            radioAfrica.id -> return CONTINENT.AFRICA
            radioOceania.id -> return CONTINENT.OCEANIA

            else -> {
                return CONTINENT.GLOBAL
            }
        }
    }
}
