package me.wozappz.whatsthatflag.screens.menu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.radio_group_table_layout.*
import org.jetbrains.anko.startActivity
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.widget.TextView
import me.wozappz.whatsthatflag.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.find

class MenuActivity : AppCompatActivity(), me.wozappz.whatsthatflag.screens.menu.MenuScreenContract.View {

    override lateinit var presenter: me.wozappz.whatsthatflag.screens.menu.MenuScreenContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.setBackgroundColor(Color.BLACK)

        setupUiElements()

        presenter = me.wozappz.whatsthatflag.screens.menu.MenuPresenter(this)
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

        if (!mStartBtn.isClickable) {
            setStartButtonClickability(true)
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

    override fun setStartButtonClickability(isClickable: Boolean) {
        mStartBtn.isClickable = isClickable
    }

    override fun startGameActivityWithDelay(amount: Int, selectedContinent: me.wozappz.whatsthatflag.screens.menu.CONTINENT, duration: Long) {
        Handler().postDelayed({
            startGameActivity(amount, selectedContinent)
        }, duration)
    }

    private fun startGameActivity(amount: Int, selectedContinent: me.wozappz.whatsthatflag.screens.menu.CONTINENT) {
        startActivity<me.wozappz.whatsthatflag.screens.game.GameActivity>(
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

            positiveButton(getString(R.string.alert_menu_info_btn_pos)) {  }
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
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun getSelectedContinent(): me.wozappz.whatsthatflag.screens.menu.CONTINENT {
        val id = (mRadioGroupContinents as me.wozappz.whatsthatflag.screens.menu.RadioGroupTableLayout).getCheckedRadioButtonId()
        return getContinentFromId(id)
    }

    private fun getContinentFromId(radioBtnId: Int): me.wozappz.whatsthatflag.screens.menu.CONTINENT {
        when (radioBtnId) {
            mRadioGlobal.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.GLOBAL
            mRadioEurope.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.EUROPE
            mRadioAsia.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.ASIA
            mRadioAmericas.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.AMERICAS
            mRadioAfrica.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.AFRICA
            mRadioOceania.id -> return me.wozappz.whatsthatflag.screens.menu.CONTINENT.OCEANIA

            else -> {
                return me.wozappz.whatsthatflag.screens.menu.CONTINENT.GLOBAL
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
