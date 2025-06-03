package com.nocircle.app.pages.main.friends.list

import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddFriendSheetViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
}