package com.olq.whatsthatflag.screens.game

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.injector.Injector
import com.olq.whatsthatflag.screens.menu.MenuActivity
import com.olq.whatsthatflag.utils.loadUrl
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class GameActivity : AppCompatActivity(), GameScreenContract.View {

    override lateinit var presenter: GameScreenContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val amountOfCountries = intent.getIntExtra("AMOUNT_OF_COUNTRIES", 20)
        val selectedContinent = intent.getSerializableExtra("SELECTED_CONTINENT") as MenuActivity.CONTINENT

        val categoryText = selectedContinent.toString().toLowerCase().capitalize()

        categoryTextView.text = getString(R.string.category_text, categoryText)

        presenter = GamePresenter(this, Injector.provideModel())
        presenter.start(Pair(selectedContinent, amountOfCountries))

        setupListeners()
    }


    private fun setupListeners() {
        myBtnA.setOnClickListener { presenter.answerBtnClicked(myBtnA.text.toString()) }
        myBtnB.setOnClickListener { presenter.answerBtnClicked(myBtnB.text.toString()) }
        myBtnC.setOnClickListener { presenter.answerBtnClicked(myBtnC.text.toString()) }
        myBtnD.setOnClickListener { presenter.answerBtnClicked(myBtnD.text.toString()) }
    }


    override fun loadImg(currentUrl: String){
        myImgView.loadUrl(currentUrl)
    }

    override fun renameButtons(btnNames: List<String>) {
        myBtnA.text = btnNames[0]
        myBtnB.text = btnNames[1]
        myBtnC.text = btnNames[2]
        myBtnD.text = btnNames[3]
    }

    override fun showScore(score: Int) {
        scoreTextView.text = getString(R.string.score_text, score)
    }

    override fun showProgressBar() {
        myProgressBar.visibility = View.VISIBLE
        myImgView.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        myProgressBar.visibility = View.INVISIBLE
        myImgView.visibility = View.VISIBLE
    }

    override fun displayMessage(msg: String) {
        toast(msg)
    }

    override fun showSummaryDialog(score: Int, totalFlagAmount: Int) {
        val summaryDialog = alert (Appcompat) {
            title = "Summary"
            message = "You scored $score out of $totalFlagAmount"

            positiveButton("Continue", { startActivity<MenuActivity>() })
        }.build()

        summaryDialog.setCancelable(false)
        summaryDialog.setCanceledOnTouchOutside(false)
        summaryDialog.show()
    }

    override fun animateCorrectAnswer(btnName: String) {
        val buttons = listOf(myBtnA, myBtnB, myBtnC, myBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnName }!!

        val colorGreen = ContextCompat.getColor(this, R.color.green)
        val colorGray = ContextCompat.getColor(this, R.color.colorText)

        correctBtn.background.setColorFilter(colorGreen, PorterDuff.Mode.SRC)

        val timer = object : CountDownTimer(1000, 1000) {
            override fun onFinish() {
                correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                presenter.timerFinished()
            }

            override fun onTick(p0: Long) {  }
        }

        timer.start()
    }

    override fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String) {
        val buttons = listOf(myBtnA, myBtnB, myBtnC, myBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnCorrectName }!!
        val wrongBtn = buttons.find { btn -> btn.text == btnSelectedName }!!

        val colorGreen = ContextCompat.getColor(this, R.color.green)
        val colorRed = ContextCompat.getColor(this, R.color.red)
        val colorGray = ContextCompat.getColor(this, R.color.colorText)
        var isGreenTinted = false

        wrongBtn.background.setColorFilter(colorRed, PorterDuff.Mode.SRC)

        val timer = object : CountDownTimer(1200, 199) {
            override fun onFinish() {
                wrongBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)

                presenter.timerFinished()
            }

            override fun onTick(p0: Long) {
                if (isGreenTinted) {
                    correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                } else {
                    correctBtn.background.setColorFilter(colorGreen, PorterDuff.Mode.SRC)
                }

                isGreenTinted = !isGreenTinted
            }
        }

        timer.start()
    }
}
