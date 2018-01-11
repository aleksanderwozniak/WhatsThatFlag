package me.wozappz.whatsthatflag

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

        assertNotNull(spyRepo.model.totalFlagList)
    }
}