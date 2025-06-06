package com.nocircle.app.pages.main

import androidx.compose.ui.unit.dp
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : NoViewModel() {
	
	val mainSubRoute = MutableStateFlow(MainSubRoute.Home)
	
	val isLeftNavigationBarExpended = MutableStateFlow(false)
	
	val leftNavigationBarWidth = MutableStateFlow(72.dp)
}