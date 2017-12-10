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
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.widget.TextView
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.browse
import org.jetbrains.anko.find

class MenuActivity : AppCompatActivity(), MenuScreenContract.View {

    override lateinit var presenter: MenuScreenContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.setBackgroundColor(Color.BLACK)

        setupUiElements()

        presenter = MenuPresenter(this)
        presenter.start()
    }

    private fun setupUiElements() {
        showFlagSeekbarLabel(20)
        setSeekBarListener()
        setStartBtnListener()

        // Make one of radio buttons selected
        mRadioGlobal.callOnClick()
    }

    override fun onResume() {
        super.onResume()

        if (mGlobeGif.alpha != 1f) {
            presenter.restartWtfDividerAnimation()
        }
    }

    fun onGlobeClicked(view: View) {
        presenter.startGlobeAnimation()
    }

    fun onInfoClicked(view: View) {
        presenter.btnInfoClicked()
    }

    private fun setStartBtnListener() {
        mStartBtn.setOnClickListener {
            presenter.btnStartClicked()
        }
    }

    override fun startGameActivityWithDelay(amount: Int, selectedContinent: CONTINENT, duration: Long) {
        Handler().postDelayed({
            startGameActivity(amount, selectedContinent)
        }, duration)
    }

    private fun startGameActivity(amount: Int, selectedContinent: CONTINENT) {
        startActivity<GameActivity>(
                "AMOUNT_OF_COUNTRIES" to amount,
                "SELECTED_CONTINENT" to selectedContinent)

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    override fun getFlagSeekbarProgress(): Int {
        return mCountriesSeekBar.progress
    }

    override fun onBackPressed() {
        // Prevents going back to StartScreen
        moveTaskToBack(true)
    }


    override fun showAppInfo() {
        val alertLayout = layoutInflater.inflate(R.layout.dialog_info, null)
        val mSourceBtn = alertLayout.find<AppCompatButton>(R.id.mDialogBtnSource)

        val infoDialog = alert (Appcompat) {
            title = "What's that Flag?"
            customView = alertLayout

            positiveButton("Back") {  }
        }.show()


        val typeface = ResourcesCompat.getFont(this, R.font.lato)

        val infoTitle = infoDialog.find<TextView>(android.support.v7.appcompat.R.id.alertTitle)
        infoTitle.typeface = typeface

        val infoBackBtn = infoDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        infoBackBtn.typeface = typeface


        mSourceBtn.setOnClickListener { presenter.btnSourceClicked() }
    }

    override fun showGitHubSourceInBrowser() {
        val url = "https://github.com/aleksanderwozniak/WhatsThatFlag"
        browse(url)
    }

    override fun getSelectedContinent(): CONTINENT {
        val id = (mRadioGroupContinents as RadioGroupTableLayout).getCheckedRadioButtonId()
        return getContinentFromId(id)
    }

    private fun getContinentFromId(radioBtnId: Int): CONTINENT {
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

    override fun showFlagSeekbarLabel(amount: Int) {
        mSeekBarText.text = getString(R.string.countries_amount, amount.toString())
    }

    override fun showFlagSeekbarLabelAll() {
        mSeekBarText.text = getString(R.string.countries_amount, getString(R.string.countries_all))
    }

    private fun setSeekBarListener() {
        mCountriesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                presenter.flagSeekbarProgressChanged(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}
