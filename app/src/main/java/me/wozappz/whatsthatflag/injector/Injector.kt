package me.wozappz.whatsthatflag.injector

import android.content.Context

/**
 * Created by olq on 20.11.17.
 */

object Injector {

    fun provideModel(ctx: Context): me.wozappz.whatsthatflag.data.Model {
        return me.wozappz.whatsthatflag.data.Model.getInstance(me.wozappz.whatsthatflag.data.Downloader(ctx))
    }
}