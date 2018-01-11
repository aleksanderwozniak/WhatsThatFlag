package me.wozappz.whatsthatflag.data.prefs

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

/**
 * Created by olq on 11.01.18.
 */
class SharedPrefsRepositoryImpl @Inject constructor(ctx: Context)
    : SharedPrefsRepository {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)


    override fun isDataFetched() =
            prefs.getBoolean("isDataFetched", false)

    override fun putDataFetched(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("isDataFetched", value)
        editor.apply()
    }
}