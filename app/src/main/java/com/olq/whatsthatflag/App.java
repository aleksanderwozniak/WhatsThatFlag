package com.olq.whatsthatflag;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by olq on 29.11.17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
    }
}
