package me.wozappz.whatsthatflag.screens.start

import me.wozappz.whatsthatflag.data.Repository
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


/**
 * Created by olq on 20.11.17.
 */
class StartPresenter @Inject constructor(
        private val view: StartScreenContract.View,
        private val repo: Repository)
    : StartScreenContract.Presenter {


    override fun start() {
        repo.model.loadTotalFlagList()

        offlineModeSetup()
    }


    private fun offlineModeSetup() {
        val isDataFetched = repo.prefsRepo.isDataFetched()

        if (view.isConnectedToInternet()) {
            fetchFlagsForOfflineMode()

            notifyUserOnce(isDataFetched)

            startMenuActivityDelayed()

        } else {
            manageOfflineModeAvailability(isDataFetched)
        }
    }

    private fun fetchFlagsForOfflineMode() {
        repo.model.fetchFlags()
    }

    private fun notifyUserOnce(isDataFetched: Boolean) {
        if (!isDataFetched) {
            view.displayOfflineModeMessage()

            repo.prefsRepo.putDataFetched(true)
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