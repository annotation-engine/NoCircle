package com.nocircle.app.pages.main

import com.nocircle.app.NoRoutes
import com.nocircle.common.navigation.NoRoute
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : NoViewModel() {
	
	val mainRoute = MutableStateFlow<NoRoute>(NoRoutes.Main.Home)
}