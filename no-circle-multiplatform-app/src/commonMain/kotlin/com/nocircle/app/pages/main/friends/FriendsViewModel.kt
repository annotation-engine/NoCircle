package com.nocircle.app.pages.main.friends

import androidx.compose.ui.unit.dp
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FriendsViewModel : NoViewModel() {
	
	val leftWidth = MutableStateFlow(240.dp)
	
	val current = MutableStateFlow<Int?>(null)
}