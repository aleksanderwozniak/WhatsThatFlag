package com.olq.whatsthatflag.screens.game

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.injector.Injector
import com.olq.whatsthatflag.screens.menu.MenuActivity
import com.olq.whatsthatflag.utils.checkInternetConnection
import com.olq.whatsthatflag.utils.loadUrl
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class GameActivity : AppCompatActivity(), GameScreenContract.View {

    override lateinit var presenter: GameScreenContract.Presenter
    private var isAnswerTimerInitialized = false
    private var answerTimer: CountDownTimer? = null
    private var animationTimer: CountDownTimer? = null


    private fun createAnswerTimer(timeForAnswer: Int) : CountDownTimer {
        return object : CountDownTimer(timeForAnswer.toLong(), 10) {
            override fun onFinish() {
                presenter.answerTimerFinished()
            }

            override fun onTick(timeTillFinished: Long) {
                val progress = timeForAnswer - timeTillFinished
                updateAnswerTimerProgressBar(progress.toInt())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val amountOfCountries = intent.getIntExtra("AMOUNT_OF_COUNTRIES", 20)
        val selectedContinent = intent.getSerializableExtra("SELECTED_CONTINENT") as MenuActivity.CONTINENT

        val categoryText = selectedContinent.toString().toLowerCase().capitalize()

        mCategoryTextView.text = getString(R.string.category_text, categoryText)

        presenter = GamePresenter(this, Injector.provideModel())
        presenter.start(Pair(selectedContinent, amountOfCountries))

        setupListeners()

        val timeForAnswer = resources.getInteger(R.integer.answer_time)
        answerTimer = createAnswerTimer(timeForAnswer)
    }

    override fun onResume() {
        super.onResume()

        if (answerTimer != null && isAnswerTimerInitialized) {
            presenter.redownloadImg(true) // prevents cheating
            resetAnswerButtonsColor()
        } else {
            isAnswerTimerInitialized = true
        }
    }

    override fun onStop() {
        super.onStop()
        answerTimer?.cancel()
        animationTimer?.cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        answerTimer?.cancel()
        animationTimer?.cancel()
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


    override fun loadImg(currentUrl: String){
        mImgView.loadUrl(currentUrl)
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
        mImgView.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.INVISIBLE
        mImgView.visibility = View.VISIBLE
    }

    override fun displayFlagInfoInBrowser(url: String) {
        browse(url)
    }

    override fun displayMessage(msg: String) {
        toast(msg)
    }

    override fun showSummaryDialog(score: Int, totalFlagAmount: Int) {
        val percent: Int = (score * 100) / totalFlagAmount

        val summaryDialog = alert (Appcompat) {
            title = "Summary"
            message = "You scored $score out of $totalFlagAmount ($percent%)"

            positiveButton("Continue", {
                finish()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            })
        }.build()

        summaryDialog.setCancelable(false)
        summaryDialog.setCanceledOnTouchOutside(false)
        summaryDialog.show()
    }

    override fun animateCorrectAnswer(btnName: String, staticAnimation: Boolean) {
        val buttons = listOf(mBtnA, mBtnB, mBtnC, mBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnName }!!

        val colorGreen = ContextCompat.getColor(this, R.color.green)
        val colorGray = ContextCompat.getColor(this, R.color.bluishGray)
        var isGreenTinted = false

        if (staticAnimation) {
            correctBtn.background.setColorFilter(colorGreen, PorterDuff.Mode.SRC)

            animationTimer = object : CountDownTimer(1000, 1000) {
                override fun onFinish() {
                    correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                    presenter.animationTimerFinished()
                }

                override fun onTick(p0: Long) {}
            }.start()

        } else {
            animationTimer = object : CountDownTimer(1200, 199) {
                override fun onFinish() {
                    correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                    presenter.animationTimerFinished()
                }

                override fun onTick(p0: Long) {
                    isGreenTinted = blinkingAnimation(correctBtn, colorGray, colorGreen, isGreenTinted)
                }
            }.start()
        }
    }

    override fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String) {
        val buttons = listOf(mBtnA, mBtnB, mBtnC, mBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnCorrectName }!!
        val wrongBtn = buttons.find { btn -> btn.text == btnSelectedName }!!

        val colorGreen = ContextCompat.getColor(this, R.color.green)
        val colorRed = ContextCompat.getColor(this, R.color.red)
        val colorGray = ContextCompat.getColor(this, R.color.bluishGray)
        var isGreenTinted = false

        wrongBtn.background.setColorFilter(colorRed, PorterDuff.Mode.SRC)

        animationTimer = object : CountDownTimer(1200, 199) {
            override fun onFinish() {
                wrongBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)

                presenter.animationTimerFinished()
            }

            override fun onTick(p0: Long) {
                isGreenTinted = blinkingAnimation(correctBtn, colorGray, colorGreen, isGreenTinted)
            }
        }.start()
    }

    private fun blinkingAnimation(btn: Button, colorNormal: Int, colorTint: Int, isTinted: Boolean): Boolean {
        if (isTinted) {
            btn.background.setColorFilter(colorNormal, PorterDuff.Mode.SRC)
        } else {
            btn.background.setColorFilter(colorTint, PorterDuff.Mode.SRC)
        }

        return !isTinted
    }

    private fun resetAnswerButtonsColor() {
        val colorGray = ContextCompat.getColor(this, R.color.bluishGray)
        mBtnA.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnB.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnC.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
        mBtnD.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
    }

    override fun setButtonsClickability(enabled: Boolean) {
        mBtnA.isClickable = enabled
        mBtnB.isClickable = enabled
        mBtnC.isClickable = enabled
        mBtnD.isClickable = enabled
    }

    override fun isConnectedToInternet(): Boolean {
        return checkInternetConnection(applicationContext)
    }

    override fun showNoConnectionAlert() {
        val internetErrorAlert = alert (Appcompat) {
            title = "Connection error!"
            message = "Make sure you are connected to Internet"

            positiveButton("Refresh", { presenter.redownloadImg() })
            negativeButton("Exit App", { finishAffinity() })
        }.build()

        internetErrorAlert.setCancelable(false)
        internetErrorAlert.setCanceledOnTouchOutside(false)
        internetErrorAlert.show()
    }


    override fun startAnswerTimer() {
        answerTimer?.start()
    }

    override fun stopAnswerTimer() {
        answerTimer?.cancel()
    }

    private fun updateAnswerTimerProgressBar(progress: Int) {
        mTimerProgressBar.progress = progress
    }
}
