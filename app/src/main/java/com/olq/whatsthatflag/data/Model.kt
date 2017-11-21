package com.olq.whatsthatflag.data

import com.olq.whatsthatflag.screens.menu.MenuActivity
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


    private var totalFlagList = listOf<String>()
    var flagList = mutableListOf<String>()



    fun downloadAllFlags() {
        val flags = mutableListOf<String>()
        downloader.downloadContinent(MenuActivity.CONTINENT.GLOBAL, flags)

        totalFlagList = flags
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


    fun selectFlags(gameData: Pair<MenuActivity.CONTINENT, Int>) {
        var dupFlagList = totalFlagList.toMutableList() // duplicate contents of totalFlagList

        if (gameData.first != MenuActivity.CONTINENT.GLOBAL) {
            val id = calculateContinentId(gameData.first)
            val from = if (id == 0) 0 else downloader.continentIds[id - 1]
            val to = downloader.continentIds[id]

            dupFlagList = dupFlagList.subList(from, to) // select a particular continent
        }

        Collections.shuffle(dupFlagList)

        flagList = if (gameData.second != -1) dupFlagList.subList(0, gameData.second) else dupFlagList
    }


    private fun calculateContinentId(continent: MenuActivity.CONTINENT): Int {
        when (continent) {
            MenuActivity.CONTINENT.AFRICA -> return 0
            MenuActivity.CONTINENT.ASIA -> return 1
            MenuActivity.CONTINENT.EUROPE -> return 2
            MenuActivity.CONTINENT.AMERICAS -> return 3
            MenuActivity.CONTINENT.OCEANIA -> return 4
            else -> return 0 // can't happen
        }
    }
}