package com.nocircle.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nocircle.common.utils.Globals

class NoCircleActivity : ComponentActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Globals.setActivity(this)
		setContent { NoApp() }
	}
	
	override fun onDestroy() {
		super.onDestroy()
		Globals.clearActivity()
	}
}