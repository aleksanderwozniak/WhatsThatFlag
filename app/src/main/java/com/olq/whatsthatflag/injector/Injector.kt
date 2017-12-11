package com.olq.whatsthatflag.injector

import android.content.Context
import com.olq.whatsthatflag.data.Downloader
import com.olq.whatsthatflag.data.Model

/**
 * Created by olq on 20.11.17.
 */

object Injector {

    fun provideModel(ctx: Context): Model {
        return Model.getInstance(Downloader(ctx))
    }
}