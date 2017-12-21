package me.wozappz.whatsthatflag

import android.app.Application
import com.squareup.leakcanary.LeakCanary

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
    }
}