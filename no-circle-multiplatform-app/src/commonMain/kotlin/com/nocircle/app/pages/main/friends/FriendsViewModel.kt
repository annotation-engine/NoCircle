package com.nocircle.app.pages.main.friends

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FriendsViewModel : NoViewModel() {
	
	val contentWidth = MutableStateFlow(240.dp)
	
	init {
		viewModelScope.launch {
		
		}
	}
	
	private fun loadFriendList() {
	
	}
}