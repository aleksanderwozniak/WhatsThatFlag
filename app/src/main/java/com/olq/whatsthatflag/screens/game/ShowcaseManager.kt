package com.olq.whatsthatflag.screens.game

import android.support.v4.content.ContextCompat
import com.olq.whatsthatflag.R
import kotlinx.android.synthetic.main.activity_game.*
import me.toptas.fancyshowcase.DismissListener
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView
import me.toptas.fancyshowcase.FocusShape

/**
 * Created by olq on 16.12.17.
 */
class ShowcaseManager (private val gameActivity: GameActivity) {

    private val maskColor = ContextCompat.getColor(gameActivity, R.color.colorAccentTransparent)

    fun showTutorial() {
        val showcaseWtf = wtfShowcaseBuilder().build()
        val showcaseQuestions = questionsShowcaseBuilder().build()
        val showcaseScore = scoreShowcaseBuilder().build()

        val showcaseQueue = FancyShowCaseQueue()
                .add(showcaseQuestions)
                .add(showcaseScore)
                .add(showcaseWtf)

        showcaseQueue.show()
    }

    private fun wtfShowcaseBuilder() = FancyShowCaseView.Builder(gameActivity)
                .focusOn(gameActivity.mWTFbtn)
                .title("Magic button")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(72)
                .fitSystemWindows(true)
                .backgroundColor(maskColor)
                .showOnce("TutorialShowcase1")
                .dismissListener(object : DismissListener {
                    override fun onSkipped(id: String?) {
                        gameActivity.setupAnswerTimer()
                    }

                    override fun onDismiss(id: String?) {
                        gameActivity.setupAnswerTimer()
                        gameActivity.startAnswerTimer()
                    }
                })

    private fun questionsShowcaseBuilder() = FancyShowCaseView.Builder(gameActivity)
            .focusOn(gameActivity.mQuestionsTextView)
            .title("Remaining flags")
            .focusShape(FocusShape.CIRCLE)
            .fitSystemWindows(true)
            .backgroundColor(maskColor)
            .showOnce("TutorialShowcase2")

    private fun scoreShowcaseBuilder() = FancyShowCaseView.Builder(gameActivity)
            .focusOn(gameActivity.mScoreTextView)
            .title("Your score")
            .focusShape(FocusShape.CIRCLE)
            .fitSystemWindows(true)
            .backgroundColor(maskColor)
            .showOnce("TutorialShowcase3")
}