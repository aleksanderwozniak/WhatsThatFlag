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
import me.wozappz.whatsthatflag.app.App
import me.wozappz.whatsthatflag.di.menu.MenuScreenModule
import me.wozappz.whatsthatflag.screens.game.GameActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

class MenuActivity : AppCompatActivity(), MenuScreenContract.View {

    @Inject override lateinit var presenter: MenuScreenContract.Presenter

    private val animManager by lazy { MenuAnimationManager(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.setBackgroundColor(Color.BLACK)

        setupUiElements()

        (application as App).daggerComponent
                .plus(MenuScreenModule(this))
                .injectTo(this)


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

    override fun startGameActivityWithDelay(selectedContinent: CONTINENT, duration: Long) {
        Handler().postDelayed({
            startGameActivity(selectedContinent)
        }, duration)
    }

    private fun startGameActivity(selectedContinent: CONTINENT) {
        startActivity<GameActivity>(
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

    override fun displayMessageOceaniaMaxFlags(amount: Int) {
        toast(getString(R.string.toast_game_oceania_max_flags, amount))
    }

    override fun animateViewsAlpha(alphaValue: Float, duration: Long) {
        animManager.animateViewsAlpha(alphaValue, duration)
    }

    override fun setupGlobeAnimation() {
        animManager.setupGlobeAnimation()
    }

    override fun showWtfDivider(duration: Long, delay: Long) {
        animManager.showWtfDivider(duration, delay)
    }

    override fun hideWtfDivider(duration: Long, delay: Long) {
        animManager.hideWtfDivider(duration, delay)
    }

    override fun runCompoundGlobeAnimation() {
        animManager.compoundGlobeAnimation()
    }

    override fun animateBackgroundColor() {
        animManager.animateBackgroundColor()
    }
}
