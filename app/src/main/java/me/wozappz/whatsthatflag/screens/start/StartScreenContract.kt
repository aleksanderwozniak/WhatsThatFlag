package me.wozappz.whatsthatflag.screens.start

import me.wozappz.whatsthatflag.screens.BaseView

/**
 * Created by olq on 20.11.17.
 */
interface StartScreenContract {

    interface View: BaseView<Presenter> {
        fun startMenuActivity()
        fun isConnectedToInternet(): Boolean
        fun showNoConnectionAlert()
    }

    interface Presenter {
        fun start()
    }
}