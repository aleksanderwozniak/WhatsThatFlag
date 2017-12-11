package com.olq.whatsthatflag.screens.start

import com.olq.whatsthatflag.data.Model
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by olq on 20.11.17.
 */
class StartPresenter(private val view: StartScreenContract.View,
                     private val model: Model)
    : StartScreenContract.Presenter {


    override fun start() {
        if (view.isConnectedToInternet()) {

            val viewRef: Ref<StartScreenContract.View> = view.asReference()

            async(UI) {
                bg {
                    model.loadTotalFlagList()
                }.await()

                viewRef.invoke().startMenuActivity()
            }

        } else {
            view.showNoConnectionAlert()
        }
    }
}