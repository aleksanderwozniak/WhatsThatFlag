package me.wozappz.whatsthatflag.screens.game

import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import me.wozappz.whatsthatflag.R
import me.wozappz.whatsthatflag.utils.checkInternetConnection
import me.wozappz.whatsthatflag.utils.loadUrl
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class GameActivity : AppCompatActivity(), me.wozappz.whatsthatflag.screens.game.GameScreenContract.View {

    override lateinit var presenter: me.wozappz.whatsthatflag.screens.game.GameScreenContract.Presenter
    private var isAnswerTimerInitialized = false

    private val animationManager by lazy { me.wozappz.whatsthatflag.screens.game.AnswerAnimationManager(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val amountOfCountries = intent.getIntExtra("AMOUNT_OF_COUNTRIES", 20)
        val selectedContinent = intent.getSerializableExtra("SELECTED_CONTINENT") as me.wozappz.whatsthatflag.screens.menu.CONTINENT

        val lowercaseContinent = selectedContinent.toString().toLowerCase()
        val continentStringId = convertToRes(lowercaseContinent)
        val continentResId = resources.getIdentifier(continentStringId, "string", packageName)

        val continentText = getString(continentResId)
        mCategoryTextView.text = getString(R.string.category_text, continentText)

        presenter = me.wozappz.whatsthatflag.screens.game.GamePresenter(this, me.wozappz.whatsthatflag.injector.Injector.provideModel(applicationContext))
        presenter.start(Pair(selectedContinent, amountOfCountries))

        setupListeners()

        me.wozappz.whatsthatflag.screens.game.ShowcaseManager(this).showTutorial()
    }

    fun setupAnswerTimer() {
        val timeForAnswer = resources.getInteger(R.integer.answer_time)
        animationManager.createAnswerTimer(timeForAnswer)
    }

    private fun convertToRes(continentName: String): String {
        return "radio_text_$continentName"
    }

    override fun onResume() {
        super.onResume()

        if (animationManager.answerTimerExists() && isAnswerTimerInitialized) {
            presenter.redownloadImg(true) // prevents cheating
            resetAnswerButtonsColor()
        } else {
            isAnswerTimerInitialized = true
        }
    }

    override fun onStop() {
        Picasso.with(this).cancelRequest(mFlagImg)
        super.onStop()
        animationManager.stopAnswerTimer()
        animationManager.stopAnimationTimer()
    }

    override fun onBackPressed() {
        presenter.backButtonClicked()
    }

    fun onWTFclick(view: View) {
        presenter.btnWTFclicked()
    }

    private fun setupListeners() {
        mBtnA.setOnClickListener { presenter.answerBtnClicked(mBtnA.text.toString()) }
        mBtnB.setOnClickListener { presenter.answerBtnClicked(mBtnB.text.toString()) }
        mBtnC.setOnClickListener { presenter.answerBtnClicked(mBtnC.text.toString()) }
        mBtnD.setOnClickListener { presenter.answerBtnClicked(mBtnD.text.toString()) }
    }


    override fun loadImg(currentUrl: String, callback: Callback) {
        mFlagImg.loadUrl(currentUrl, callback)
    }

    override fun renameButtons(btnNames: List<String>) {
        mBtnA.text = btnNames[0]
        mBtnB.text = btnNames[1]
        mBtnC.text = btnNames[2]
        mBtnD.text = btnNames[3]
    }

    override fun showScore(score: Int) {
        mScoreTextView.text = getString(R.string.score_text, score)
    }

    override fun showRemainingQuestions(amount: Int) {
        mQuestionsTextView.text = getString(R.string.questions_text, amount)
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
        mFlagImg.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.INVISIBLE
        mFlagImg.visibility = View.VISIBLE
    }

    override fun displayFlagInfoInBrowser(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun displayMessageOceaniaMaxFlags(amount: Int) {
        toast(getString(R.string.toast_game_oceania_max_flags, amount))
    }

    override fun displayMessageErrorLoadNextFlag() {
        toast(getString(R.string.toast_game_error_load_next_flag))
    }

    override fun displayMessageReloadImg() {
        toast(getString(R.string.toast_game_reload_img))
    }

    override fun displayMessageFlagSkipped(flagName: String) {
        toast(getString(R.string.toast_game_flag_skipped, flagName))
    }

    override fun showSummaryDialog(score: Int, totalFlagAmount: Int) {
        val percent: Int = (score * 100) / totalFlagAmount

        val summaryDialog = alert (Appcompat) {
            title = getString(R.string.alert_game_summary_title)
            message = getString(R.string.alert_game_summary_message, score, totalFlagAmount, percent)

            positiveButton(getString(R.string.alert_game_summary_btn_pos), {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            })
        }.build()

        summaryDialog.setCancelable(false)
        summaryDialog.setCanceledOnTouchOutside(false)
        summaryDialog.show()

        val typeface = ResourcesCompat.getFont(this, R.font.lato)

        val summaryTitle = summaryDialog.find<TextView>(android.support.v7.appcompat.R.id.alertTitle)
        summaryTitle.typeface = typeface

        val summaryMessage = summaryDialog.find<TextView>(android.R.id.message)
        summaryMessage.typeface = typeface

        val summaryContinueBtn = summaryDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        summaryContinueBtn.typeface = typeface
    }

    override fun animateCorrectAnswer(btnName: String, staticAnimation: Boolean) {
        animationManager.animateCorrectAnswer(btnName, staticAnimation)
    }

    override fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String) {
        animationManager.animateWrongAnswer(btnSelectedName, btnCorrectName)
    }

    private fun resetAnswerButtonsColor() {
        val colorGray = ContextCompat.getColor(this, R.color.bluishGray)
        mBtnA.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnB.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnC.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnD.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
    }

    override fun setButtonsClickability(enabled: Boolean) {
        mBtnA.isEnabled = enabled
        mBtnB.isEnabled = enabled
        mBtnC.isEnabled = enabled
        mBtnD.isEnabled = enabled
        mWTFbtn.isEnabled = enabled
    }

    override fun isConnectedToInternet(): Boolean {
        return checkInternetConnection(applicationContext)
    }

    override fun showNoConnectionAlert() {
        val internetErrorAlert = alert (Appcompat) {
            title = getString(R.string.alert_start_internet_error_title)
            message = getString(R.string.alert_game_internet_error_msg)

            positiveButton(getString(R.string.alert_game_internet_error_btn_pos), { presenter.redownloadImg() })
            negativeButton(getString(R.string.alert_game_internet_error_btn_neg), { finishAffinity() })
        }.build()

        internetErrorAlert.setCancelable(false)
        internetErrorAlert.setCanceledOnTouchOutside(false)
        internetErrorAlert.show()

        val typeface = ResourcesCompat.getFont(this, R.font.lato)

        val netErrorTitle = internetErrorAlert.find<TextView>(android.support.v7.appcompat.R.id.alertTitle)
        netErrorTitle.typeface = typeface

        val netErrorMessage = internetErrorAlert.find<TextView>(android.R.id.message)
        netErrorMessage.typeface = typeface

        val netErrorPosBtn = internetErrorAlert.getButton(AlertDialog.BUTTON_POSITIVE)
        netErrorPosBtn.typeface = typeface

        val netErrorNegBtn = internetErrorAlert.getButton(AlertDialog.BUTTON_NEGATIVE)
        netErrorNegBtn.typeface = typeface
    }


    override fun showBackToMenuDialog() {
        val backDialog = alert (Appcompat) {
            title = getString(R.string.alert_game_back_title)
            message = getString(R.string.alert_game_back_message)

            positiveButton(getString(R.string.alert_game_back_btn_pos), {
                super.onBackPressed()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                animationManager.stopAnswerTimer()
                animationManager.stopAnimationTimer()
            })
            negativeButton(getString(R.string.alert_game_back_btn_neg), { })
        }.build()

        backDialog.setCancelable(false)
        backDialog.setCanceledOnTouchOutside(false)
        backDialog.show()

        val typeface = ResourcesCompat.getFont(this, R.font.lato)

        val backTitle = backDialog.find<TextView>(android.support.v7.appcompat.R.id.alertTitle)
        backTitle.typeface = typeface

        val backMessage = backDialog.find<TextView>(android.R.id.message)
        backMessage.typeface = typeface

        val backPosBtn = backDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        backPosBtn.typeface = typeface

        val backNegBtn = backDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        backNegBtn.typeface = typeface
    }


    override fun startAnswerTimer() {
        animationManager.startAnswerTimer()
    }

    override fun stopAnswerTimer() {
        animationManager.stopAnswerTimer()
    }

    fun updateAnswerTimerProgressBar(progress: Int) {
        mTimerProgressBar.progress = progress
    }
}
