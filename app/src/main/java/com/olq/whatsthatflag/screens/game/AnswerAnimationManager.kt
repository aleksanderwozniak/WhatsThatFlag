package com.olq.whatsthatflag.screens.game

import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.widget.Button
import com.olq.whatsthatflag.R
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Created by olq on 16.12.17.
 */
class AnswerAnimationManager (private val gameActivity: GameActivity) {

    private var answerTimer: CountDownTimer? = null
    private var animationTimer: CountDownTimer? = null


    fun animateCorrectAnswer(btnName: String, staticAnimation: Boolean) {
        val buttons = listOf(gameActivity.mBtnA, gameActivity.mBtnB, gameActivity.mBtnC, gameActivity.mBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnName }!!

        val colorGreen = ContextCompat.getColor(gameActivity, R.color.green)
        val colorGray = ContextCompat.getColor(gameActivity, R.color.bluishGray)
        var isGreenTinted = false


        if (staticAnimation) {
            correctBtn.background.setColorFilter(colorGreen, PorterDuff.Mode.SRC)

            animationTimer = object : CountDownTimer(1000, 1000) {
                override fun onFinish() {
                    correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                    gameActivity.presenter.animationTimerFinished()
                }

                override fun onTick(p0: Long) {}
            }.start()

        } else {
            animationTimer = object : CountDownTimer(1200, 199) {
                override fun onFinish() {
                    correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                    gameActivity.presenter.animationTimerFinished()
                }

                override fun onTick(p0: Long) {
                    isGreenTinted = blinkingAnimation(correctBtn, colorGray, colorGreen, isGreenTinted)
                }
            }.start()
        }
    }


    fun animateWrongAnswer(btnSelectedName: String, btnCorrectName: String) {
        val buttons = listOf(gameActivity.mBtnA, gameActivity.mBtnB, gameActivity.mBtnC, gameActivity.mBtnD)
        val correctBtn = buttons.find { btn -> btn.text == btnCorrectName }!!
        val wrongBtn = buttons.find { btn -> btn.text == btnSelectedName }!!

        val colorGreen = ContextCompat.getColor(gameActivity, R.color.green)
        val colorRed = ContextCompat.getColor(gameActivity, R.color.red)
        val colorGray = ContextCompat.getColor(gameActivity, R.color.bluishGray)
        var isGreenTinted = false

        wrongBtn.background.setColorFilter(colorRed, PorterDuff.Mode.SRC)

        animationTimer = object : CountDownTimer(1200, 199) {
            override fun onFinish() {
                wrongBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)
                correctBtn.background.setColorFilter(colorGray, PorterDuff.Mode.SRC)

                gameActivity.presenter.animationTimerFinished()
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


    fun createAnswerTimer(timeForAnswer: Int) {
        answerTimer = object : CountDownTimer(timeForAnswer.toLong(), 10) {
            override fun onFinish() {
                gameActivity.presenter.answerTimerFinished()
            }

            override fun onTick(timeTillFinished: Long) {
                val progress = timeForAnswer - timeTillFinished
                gameActivity.updateAnswerTimerProgressBar(progress.toInt())
            }
        }
    }


    fun startAnswerTimer() {
        answerTimer?.start()
    }

    fun stopAnswerTimer() {
        answerTimer?.cancel()
    }

    fun stopAnimationTimer() {
        animationTimer?.cancel()
    }

    fun answerTimerExists() : Boolean {
        return answerTimer != null
    }
}