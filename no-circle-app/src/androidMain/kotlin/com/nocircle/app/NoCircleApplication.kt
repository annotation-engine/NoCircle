package com.nocircle.app

import android.app.Application
import com.nocircle.common.utils.Globals
import java.lang.ref.WeakReference

class NoCircleApplication : Application() {
	
	override fun onCreate() {
		super.onCreate()
		Globals.applicationContext = WeakReference(this)
	}
	
	override fun onTerminate() {
		super.onTerminate()
		Globals.applicationContext?.clear()
		Globals.applicationContext = null
	}
}