package me.wozappz.whatsthatflag.data

import me.wozappz.whatsthatflag.screens.menu.CONTINENT
import java.util.*

/**
 * Created by olq on 20.11.17.
 */

class Model (private val downloader: Downloader) {

    companion object {
        private var instance: Model? = null

        @Synchronized
        fun getInstance(downloader: Downloader): Model {
            if (instance == null) {
                instance = Model(downloader)
            }

            return instance!!
        }
    }


    lateinit private var continentSpliterator: IntArray
    private var totalFlagList = listOf<String>()
    var flagList = mutableListOf<String>()



    fun loadTotalFlagList() {
        totalFlagList = downloader.loadAllFlagsFromRes()
        loadContinentSpliterator()
    }

    private fun loadContinentSpliterator() {
        continentSpliterator = downloader.loadContinentSpliteratorFromRes()
    }


    fun getImgUrl(urlString: String): String? {
        return downloader.getImgURL(urlString)
    }


    fun getButtonNames(flagId: Int): List<String> {
        val dupFlagList = flagList.toMutableList()
        val currentFlagCountry = dupFlagList[flagId]

        dupFlagList.remove(currentFlagCountry)
        Collections.shuffle(dupFlagList)

        val btnNames = dupFlagList.take(3) as MutableList

        btnNames.add(currentFlagCountry)
        Collections.shuffle(btnNames)

        return btnNames
    }


    fun selectFlags(gameData: Pair<CONTINENT, Int>) {
        var dupFlagList = totalFlagList.toMutableList() // duplicate contents of totalFlagList

        if (gameData.first != CONTINENT.GLOBAL) {
            val id = calculateContinentId(gameData.first)
            val from = if (id == 0) 0 else continentSpliterator[id - 1]
            val to = continentSpliterator[id]

            dupFlagList = dupFlagList.subList(from, to) // select a particular continent
        }

        Collections.shuffle(dupFlagList)

        // MARK: quickfix - Oceania has only 23 countries, while user can easily select 40 in menu
        val totalAmountOfFlags = if (gameData.first == CONTINENT.OCEANIA && gameData.second == 40) dupFlagList.size else gameData.second
        flagList = if (totalAmountOfFlags != -1) dupFlagList.subList(0, totalAmountOfFlags) else dupFlagList
    }


    private fun calculateContinentId(continent: CONTINENT): Int {
        when (continent) {
            CONTINENT.AFRICA -> return 0
            CONTINENT.ASIA -> return 1
            CONTINENT.EUROPE -> return 2
            CONTINENT.AMERICAS -> return 3
            CONTINENT.OCEANIA -> return 4
            else -> return 0 // can't happen
        }
    }

    fun getURLFromName(countryName: String): String {
        val validCountryName: String = countryName.replace("[ ]".toRegex(), "_")
        return downloader.getWikipediaLink(validCountryName)
    }
}