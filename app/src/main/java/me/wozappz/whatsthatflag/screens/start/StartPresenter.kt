package me.wozappz.whatsthatflag.screens.start

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by olq on 20.11.17.
 */
class StartPresenter(private val view: me.wozappz.whatsthatflag.screens.start.StartScreenContract.View,
                     private val model: me.wozappz.whatsthatflag.data.Model)
    : me.wozappz.whatsthatflag.screens.start.StartScreenContract.Presenter {


    override fun start() {
        if (view.isConnectedToInternet()) {

            val viewRef: Ref<me.wozappz.whatsthatflag.screens.start.StartScreenContract.View> = view.asReference()

            async(UI) {
                bg {
                    model.loadTotalFlagList()
                }.await()

                delay(1250)
                viewRef.invoke().startMenuActivity()
            }

        } else {
            view.showNoConnectionAlert()
        }
    }
}