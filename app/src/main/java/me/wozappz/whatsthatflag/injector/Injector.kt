package me.wozappz.whatsthatflag.injector

import android.content.Context
import me.wozappz.whatsthatflag.data.Downloader
import me.wozappz.whatsthatflag.data.Model

/**
 * Created by olq on 20.11.17.
 */

object Injector {

    fun provideModel(ctx: Context): Model {
        return Model.getInstance(Downloader(ctx))
    }
}