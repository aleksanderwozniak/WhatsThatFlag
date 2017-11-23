package com.olq.whatsthatflag.screens.start

import com.olq.whatsthatflag.data.Model
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by olq on 20.11.17.
 */
class StartPresenter(private val view: StartScreenContract.View,
                     private val model: Model)
    : StartScreenContract.Presenter {


    override fun start() {
        if (view.isConnectedToInternet()) {
            doAsync {
                model.downloadAllFlags()

                onComplete {
                    view.startMenuActivity()
                }
            }

        } else {
            view.showNoConnectionAlert()
        }
    }
}