package com.nocircle.app.pages.main.friends.list

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FriendsListViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	private val _friendList = MutableStateFlow<List<FriendDTO>>(emptyList())
	val friendList = _friendList.asStateFlow()
	
	val sortOrder = MutableStateFlow(SortOrder.ASC)
	val sortBy = MutableStateFlow(SortBy.Nickname)
	
	enum class SortOrder {
		ASC,
		DESC
	}
	
	enum class SortBy {
		Nickname,
		CreateTime
	}
	
	init {
		viewModelScope.launch {
			async { loadFriendList() }
			async {
			
			}
		}
	}
	
	suspend fun loadFriendList() {
		
		val result = ktorfitx.friendApi.queryFriendList()
		if (result.success) {
		
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
}