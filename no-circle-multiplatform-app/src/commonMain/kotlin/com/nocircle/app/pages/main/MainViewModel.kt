package com.nocircle.app.pages.main

import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : NoViewModel() {
	
	val mainSubPage = MutableStateFlow(MainSubPage.Home)
}