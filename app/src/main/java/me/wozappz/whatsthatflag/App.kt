package me.wozappz.whatsthatflag

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader

/**
 * Created by olq on 10.12.17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)


        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))

        val picasso = builder.build()

        // for debugging
//        picasso.setIndicatorsEnabled(true)
//        picasso.isLoggingEnabled = true

        Picasso.setSingletonInstance(picasso)
    }
}