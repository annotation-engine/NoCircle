package com.nocircle.app

import android.app.Application
import com.nocircle.common.utils.Globals

class NoCircleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Globals.setApplicationContext(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Globals.clearApplicationContext()
    }
}