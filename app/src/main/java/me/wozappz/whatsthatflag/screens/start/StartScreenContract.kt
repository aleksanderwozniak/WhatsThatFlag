package me.wozappz.whatsthatflag.screens.start

/**
 * Created by olq on 20.11.17.
 */
interface StartScreenContract {

    interface View: me.wozappz.whatsthatflag.screens.BaseView<me.wozappz.whatsthatflag.screens.start.StartScreenContract.Presenter> {
        fun startMenuActivity()
        fun isConnectedToInternet(): Boolean
        fun showNoConnectionAlert()
    }

    interface Presenter {
        fun start()
    }
}