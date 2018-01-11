package me.wozappz.whatsthatflag.presenters

import me.wozappz.whatsthatflag.data.model.Model
import me.wozappz.whatsthatflag.screens.menu.MenuPresenter
import me.wozappz.whatsthatflag.screens.menu.MenuScreenContract
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by olq on 11.01.18.
 */

@RunWith(MockitoJUnitRunner::class)
class MenuPresenterTest {

    @Mock
    lateinit var mockView: MenuScreenContract.View

    @Mock
    lateinit var mockModel: Model

    @InjectMocks
    lateinit var presenter: MenuPresenter


    @Test
    fun start_preparesAnimations() {
        presenter.start()

        verify(mockView).setupGlobeAnimation()
        verify(mockView).animateViewsAlpha(anyFloat(), eq(0L))
    }

    @Test
    fun restartWtfDividerAnimation_test() {
        presenter.restartWtfDividerAnimation()

        verify(mockView).hideWtfDivider(anyLong(), anyLong())
        verify(mockView).showWtfDivider(anyLong(), anyLong())
    }

    @Test
    fun startGlobeAnimation_showsViews() {
        presenter.startGlobeAnimation()

        verify(mockView).animateViewsAlpha(eq(1f), anyLong())
    }

    @Test
    fun startGlobeAnimation_animatesBackground() {
        presenter.startGlobeAnimation()

        verify(mockView).animateBackgroundColor()
    }

    @Test
    fun startGlobeAnimation_runsCompoundGlobeAnimation() {
        presenter.startGlobeAnimation()

        verify(mockView).runCompoundGlobeAnimation()
    }

    @Test
    fun btnInfoClicked_showsAppDetails() {
        presenter.btnInfoClicked()

        verify(mockView).showAppInfo()
    }

    @Test
    fun btnSourceClicked_showsGitHubPage() {
        presenter.btnSourceClicked()

        verify(mockView).showGitHubSourceInBrowser()
    }

    @Test
    fun flagSeekbarProgressChanged_calculatesProperValue() {
        val progress = 1 // allowed values = 0-4
        val amount = 10 // progress of 1 is converted to amount of countries of 10

        presenter.flagSeekbarProgressChanged(progress)

        verify(mockView).showFlagSeekbarLabel(amount)
    }

    @Test
    fun flagSeekbarProgressChanged_showsAllLabel() {
        val progress = 4 // ALL countries

        presenter.flagSeekbarProgressChanged(progress)

        verify(mockView).showFlagSeekbarLabelAll()
    }
}