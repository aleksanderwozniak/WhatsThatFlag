package me.wozappz.whatsthatflag

import junit.framework.Assert.*
import me.wozappz.whatsthatflag.data.model.DataLoader
import me.wozappz.whatsthatflag.data.model.ModelImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by olq on 11.01.18.
 */

@RunWith(MockitoJUnitRunner::class)
class ModelTest {

    @Mock
    lateinit var mockDataLoader: DataLoader

    @InjectMocks
    lateinit var model: ModelImpl


    @Test
    fun loadTotalFlags_loadsFlags() {
        model.loadTotalFlagList()

        verify(mockDataLoader).getWtfFlagList()
        assertNotNull(model.totalFlagList)
    }

    @Test
    fun loadTotalFlags_loadsAllFlags() {
        val amount = 3

        `when`(mockDataLoader.getWtfFlagList()).then {
            listOf(Pair("A", "B"), Pair("C", "D"), Pair("E", "F"))
        }

        model.loadTotalFlagList()
        assertEquals(amount, model.totalFlagList.size)
    }

    @Test
    fun getURLFromName_validatesCountryName() {
        val expected = "United_Kingdom"
        val input = "United Kingdom"

        model.getURLFromName(input)

        verify(mockDataLoader).getWikipediaLink(expected)
    }

    @Test
    fun fetchFlags_fetchesAllFlags() {
        model.totalFlagList = listOf(Pair("A", "B"), Pair("C", "D"), Pair("E", "F"))

        model.fetchFlags()

        verify(mockDataLoader, times(1)).fetchFlag("B")
        verify(mockDataLoader, times(1)).fetchFlag("D")
        verify(mockDataLoader, times(1)).fetchFlag("F")
    }

    @Test
    fun getButtonNames_returnsValidList() {
        model.flagList = listOf(
                Pair("Albania", "FlagAlbania"),
                Pair("Bermuda", "FlagBermuda"),
                Pair("France", "FlagFrance"),
                Pair("Italy", "FlagItaly"),
                Pair("Spain", "FlagSpain"),
                Pair("Zimbabwe", "FlagZimbabwe"))

        val btnNames = model.getButtonNames(1)

        assertTrue(btnNames.contains("Bermuda"))
        assertFalse(btnNames.contains("FlagBermuda"))
        assertEquals(4, btnNames.size)
    }
}