package com.olq.whatsthatflag

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity() {

    enum class CONTINENT{
        GLOBAL,
        EUROPE,
        ASIA,
        AFRICA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

//        Log.d("StartActivity", "radioGroupContintents.checkedRadioButtonId = ${radioGroupContintents.checkedRadioButtonId}")

        myCountriesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                val amount = calculateAmountOfCountries(p1)

                if (amount == -1) {
                    mySeekBarText.text = "Countries: All"
                } else {
                    mySeekBarText.text = "Countries: $amount"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    fun calculateAmountOfCountries(progress: Int): Int {
        when(progress){
            0 -> return 5
            1 -> return 10
            2 -> return 20
            3 -> return 40
            4 -> return -1

            else -> { return -1 }
        }
    }

    fun onStartBtnClick(view: View){
        val amount = calculateAmountOfCountries(myCountriesSeekBar.progress)
        val selectedContinent = getSelectedContinent(radioGroupContintents.checkedRadioButtonId)

        startActivity<MainActivity>(
                "AMOUNT_OF_COUNTRIES" to amount,
                "SELECTED_CONTINENT" to selectedContinent)
    }

    fun getSelectedContinent(radioBtnId: Int): CONTINENT{
        when (radioBtnId){
            radioGlobal.id -> return CONTINENT.GLOBAL
            radioEurope.id -> return CONTINENT.EUROPE
//            radioAsia.id -> return CONTINENT.ASIA
            radioAsia.id -> return CONTINENT.AFRICA

            else -> {
                return CONTINENT.GLOBAL
            }
        }
    }
}
