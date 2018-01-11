package me.wozappz.whatsthatflag.data.model

import me.wozappz.whatsthatflag.screens.menu.CONTINENT

/**
 * Created by olq on 11.01.18.
 */
interface Model {

    val totalFlagList: List<Pair<String, String>>
    val flagList: List<Pair<String, String>>

    fun loadTotalFlagList()
    fun selectFlags(gameData: Pair<CONTINENT, Int>)
    fun getButtonNames(flagId: Int): List<String>
    fun getURLFromName(countryName: String): String
    fun fetchFlags()
}