package me.wozappz.whatsthatflag.presenters

import junit.framework.Assert.assertNotNull
import me.wozappz.whatsthatflag.data.Repository
import me.wozappz.whatsthatflag.data.model.Model
import me.wozappz.whatsthatflag.data.prefs.SharedPrefsRepository
import me.wozappz.whatsthatflag.screens.start.StartPresenter
import me.wozappz.whatsthatflag.screens.start.StartScreenContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by olq on 11.01.18.
 */
@RunWith(MockitoJUnitRunner::class)
class StartPresenterTest {

    @Mock
    lateinit var mockView: StartScreenContract.View

    @Mock
    lateinit var mockModel: Model

    @Mock
    lateinit var mockPrefs: SharedPrefsRepository


    lateinit var spyRepo: Repository
    lateinit var presenter: StartPresenter


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        spyRepo = spy(Repository(mockModel, mockPrefs))
        presenter = StartPresenter(mockView, spyRepo)
    }


    @Test
    fun start_loadsTotalFlagList() {
        `when`(spyRepo.model.loadTotalFlagList()).then {
            listOf(Pair("A", "B"))
        }

        presenter.start()

        verify(spyRepo.model).loadTotalFlagList()
        assertNotNull(spyRepo.model.totalFlagList)
    }

    @Test
    fun start_showsAlertOnNoInternetNorCache() {
        `when`(spyRepo.prefsRepo.isDataFetched()).thenReturn(false)
        `when`(mockView.isConnectedToInternet()).thenReturn(false)

        presenter.start()

        verify(spyRepo.prefsRepo).isDataFetched()
        verify(mockView).isConnectedToInternet()
        verify(mockView).showNoConnectionAlert()
    }

    @Test
    fun start_startActivityOnNoInternetButWithCache() {
        `when`(spyRepo.prefsRepo.isDataFetched()).thenReturn(true)
        `when`(mockView.isConnectedToInternet()).thenReturn(false)

        presenter.start()

        verify(spyRepo.prefsRepo).isDataFetched()
        verify(mockView).isConnectedToInternet()
        verify(mockView, after(1300)).startMenuActivity()
    }

    @Test
    fun start_notifyUserAboutOfflineMode() {
        `when`(spyRepo.prefsRepo.isDataFetched()).thenReturn(false)
        `when`(mockView.isConnectedToInternet()).thenReturn(true)

        presenter.start()

        verify(spyRepo.prefsRepo).isDataFetched()
        verify(mockView).isConnectedToInternet()
        verify(mockView).displayOfflineModeMessage()
        verify(spyRepo.model).fetchFlags()
        verify(spyRepo.prefsRepo).putDataFetched(true)
    }

    @Test
    fun start_dontNotifyUserIfDataWasFetched() {
        `when`(spyRepo.prefsRepo.isDataFetched()).thenReturn(true)
        `when`(mockView.isConnectedToInternet()).thenReturn(true)

        presenter.start()

        verify(spyRepo.prefsRepo).isDataFetched()
        verify(mockView).isConnectedToInternet()
        verify(mockView, times(0)).displayOfflineModeMessage()
        verify(spyRepo.model).fetchFlags() // fetch flags anyway, because Picasso handles caching internally
    }
}