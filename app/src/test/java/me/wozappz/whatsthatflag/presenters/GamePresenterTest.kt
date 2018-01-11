package me.wozappz.whatsthatflag.presenters

import junit.framework.Assert.assertEquals
import me.wozappz.whatsthatflag.data.model.Model
import me.wozappz.whatsthatflag.screens.game.GamePresenter
import me.wozappz.whatsthatflag.screens.game.GameScreenContract
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by olq on 11.01.18.
 */

@RunWith(MockitoJUnitRunner::class)
class GamePresenterTest {

    @Mock
    lateinit var mockView: GameScreenContract.View

    @Mock
    lateinit var mockModel: Model

    @InjectMocks
    lateinit var presenter: GamePresenter


    @Test
    fun start_resetsVariables() {
        `when`(mockModel.flagList).then {
            listOf(Pair("A", "UrlA"), Pair("B", "UrlB"))
        }

        presenter.score = 5
        presenter.currentFlagId = 3
        presenter.amountOfLoadedCountries = 1

        presenter.start()

        assertEquals(0, presenter.score)
        assertEquals(0, presenter.currentFlagId)
        assertEquals(2, presenter.amountOfLoadedCountries) // size of model.flagList
    }

    @Test
    fun start_setupsView() {
        `when`(mockModel.flagList).then {
            listOf(Pair("A", "UrlA"), Pair("B", "UrlB"))
        }

        presenter.start()

        verify(mockView).showScore(anyInt())
        verify(mockView).setButtonsClickability(false)
    }

    @Test
    fun btnWTFclicked_displaysFlagDetails() {
        `when`(mockModel.flagList).then {
            listOf(Pair("United Kingdom", "https://en.wikipedia.org/United_Kingdom"))
        }

        val name = mockModel.flagList[0].first
        val url = mockModel.getURLFromName(name)

        presenter.btnWTFclicked()

        verify(mockView).stopAnswerTimer()
        verify(mockView).displayFlagInfoInBrowser(url)
    }

    @Test
    fun answerBtnClicked_correctAnswerIncrementsScore() {
        `when`(mockModel.flagList).then {
            listOf(Pair("United Kingdom", "https://en.wikipedia.org/United_Kingdom"),
                    Pair("Italy", "https://en.wikipedia.org/Italy"))
        }

        presenter.currentFlagId = 0
        presenter.score = 0
        val selectedCountry = mockModel.flagList[0].first

        presenter.answerBtnClicked(selectedCountry)

        verify(mockView).stopAnswerTimer()
        verify(mockView).setButtonsClickability(false)

        assertEquals(1, presenter.score)
    }

    @Test
    fun answerBtnClicked_animatesCorrectAnswer() {
        `when`(mockModel.flagList).then {
            listOf(Pair("United Kingdom", "https://en.wikipedia.org/United_Kingdom"),
                    Pair("Italy", "https://en.wikipedia.org/Italy"))
        }

        presenter.currentFlagId = 0
        val correctCountry = mockModel.flagList[0].first
        val selectedCountry = mockModel.flagList[0].first

        presenter.answerBtnClicked(selectedCountry)

        verify(mockView).stopAnswerTimer()
        verify(mockView).setButtonsClickability(false)
        verify(mockView).animateCorrectAnswer(correctCountry)
        verify(mockView, times(0)).animateWrongAnswer(selectedCountry, correctCountry)
    }

    @Test
    fun answerBtnClicked_animatesWrongAnswer() {
        `when`(mockModel.flagList).then {
            listOf(Pair("United Kingdom", "https://en.wikipedia.org/United_Kingdom"),
                    Pair("Italy", "https://en.wikipedia.org/Italy"))
        }

        presenter.currentFlagId = 0
        val correctCountry = mockModel.flagList[0].first
        val selectedCountry = mockModel.flagList[1].first

        presenter.answerBtnClicked(selectedCountry)

        verify(mockView).stopAnswerTimer()
        verify(mockView).setButtonsClickability(false)
        verify(mockView).animateWrongAnswer(selectedCountry, correctCountry)
        verify(mockView, times(0)).animateCorrectAnswer(correctCountry)
    }

    @Test
    fun animationTimerFinished_showSummary() {
        presenter.currentFlagId = 4
        presenter.amountOfLoadedCountries = 5

        presenter.animationTimerFinished()

        verify(mockView).showRemainingQuestions(0)
        verify(mockView).showSummaryDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun animationTimerFinished_goToNextFlag() {
        `when`(mockModel.flagList).then {
            listOf(Pair("A", "UrlA"), Pair("B", "UrlB"), Pair("C", "UrlC"))
        }

        presenter.currentFlagId = 0
        presenter.amountOfLoadedCountries = 5

        presenter.animationTimerFinished()

        assertEquals(1, presenter.currentFlagId)
    }
}