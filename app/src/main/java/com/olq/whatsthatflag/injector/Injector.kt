package com.olq.whatsthatflag.injector

import com.olq.whatsthatflag.data.Downloader
import com.olq.whatsthatflag.data.Model

/**
 * Created by olq on 20.11.17.
 */

object Injector {

    fun provideModel(): Model {
        return Model.getInstance(Downloader())
    }
}