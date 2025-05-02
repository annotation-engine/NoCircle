package com.nocircle.app

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class NoCircleApplication : Application() {
	
	companion object {
		private var weakContext: WeakReference<Context>? = null
		
		fun getContext(): Context = requireNotNull(weakContext?.get())
	}
	
	override fun onCreate() {
		super.onCreate()
		weakContext = WeakReference(this)
	}
	
	override fun onTerminate() {
		super.onTerminate()
		weakContext?.clear()
		weakContext = null
	}
}