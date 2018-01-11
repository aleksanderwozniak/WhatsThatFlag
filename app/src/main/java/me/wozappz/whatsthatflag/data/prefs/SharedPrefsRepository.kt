package me.wozappz.whatsthatflag.data.prefs

/**
 * Created by olq on 11.01.18.
 */
interface SharedPrefsRepository {

    fun isDataFetched(): Boolean
    fun putDataFetched(value: Boolean)
}