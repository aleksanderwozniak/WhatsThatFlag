package me.wozappz.whatsthatflag.screens.start

import android.content.SharedPreferences
import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import me.wozappz.whatsthatflag.data.Model
import java.util.*
import kotlin.concurrent.schedule
import android.preference.PreferenceManager
import javax.inject.Inject


/**
 * Created by olq on 20.11.17.
 */
class StartPresenter @Inject constructor(
        private val view: StartScreenContract.View,
        private val model: Model)
    : StartScreenContract.Presenter {


    private val ctx by lazy { view as StartActivity }


    override fun start() {
        model.loadTotalFlagList()

        offlineModeSetup()
    }


    private fun offlineModeSetup() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
        val isDataFetched = prefs.getBoolean("isDataFetched", false)

        if (view.isConnectedToInternet()) {
            fetchFlagsForOfflineMode()

            notifyUserOnce(isDataFetched, prefs)

            startMenuActivityDelayed()

        } else {
            manageOfflineModeAvailability(isDataFetched)
        }
    }

    private fun fetchFlagsForOfflineMode() {
        // loads images to disk cache
        model.totalFlagList.forEach {
            Picasso.with(ctx)
                    .load(it.second)
                    .config(Bitmap.Config.RGB_565)
                    .fetch()
        }
    }

    private fun notifyUserOnce(isDataFetched: Boolean, prefs: SharedPreferences) {
        if (!isDataFetched) {
            view.displayOfflineModeMessage()

            val editor = prefs.edit()
            editor.putBoolean("isDataFetched", true)
            editor.apply()
        }
    }

    private fun startMenuActivityDelayed() {
        Timer().schedule(1250) {
            view.startMenuActivity()
        }
    }

    private fun manageOfflineModeAvailability(isDataFetched: Boolean) {
        if (isDataFetched) {
            startMenuActivityDelayed()
        } else {
            view.showNoConnectionAlert()
        }
    }
}