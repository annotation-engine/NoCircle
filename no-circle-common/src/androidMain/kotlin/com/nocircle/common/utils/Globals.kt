package com.nocircle.common.utils

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

object Globals {
	
	private var applicationContext: WeakReference<Context>? = null
	
	private var activity: WeakReference<Activity>? = null
	
	fun setApplicationContext(context: Context) {
		this.applicationContext = WeakReference(context)
	}
	
	fun getApplicationContext(): Context {
		return requireNotNull(applicationContext?.get())
	}
	
	fun clearApplicationContext() {
		this.applicationContext?.clear()
		this.applicationContext = null
	}
	
	fun setActivity(activity: Activity) {
		this.activity = WeakReference(activity)
	}
	
	fun getActivity(): Activity {
		return requireNotNull(activity?.get())
	}
	
	fun clearActivity() {
		this.activity?.clear()
		this.activity = null
	}
}