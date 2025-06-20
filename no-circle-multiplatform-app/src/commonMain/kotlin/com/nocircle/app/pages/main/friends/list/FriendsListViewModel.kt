package com.nocircle.app.pages.main.friends.list

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.FriendOrderType
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil

class FriendsListViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	private val _orderType = MutableStateFlow(FriendOrderType.PINYIN_ASC)
	val orderType = _orderType.asStateFlow()
	
	private val _friendList = MutableStateFlow<List<FriendDTO>>(emptyList())
	val friendList = _friendList.asStateFlow()
	
	private var pageNumber = 1
	private var total = 0
	
	private companion object {
		private const val PAGE_SIZE = 20
	}
	
	init {
		viewModelScope.launch {
			loadFriendList(true)
		}
	}
	
	suspend fun loadFriendList(
		reset: Boolean = false
	) {
		pageNumber = if (reset) 1 else pageNumber + 1
		if (total > 0 && pageNumber > ceil(total / PAGE_SIZE.toFloat()).toInt()) {
			pageNumber--
			return
		}
		val result = ktorfitx.friendApi.queryFriendList(pageNumber, PAGE_SIZE, orderType.value)
		if (result.success) {
			if (reset) {
				_friendList.value = result.data!!.items
			} else {
				_friendList.value = _friendList.value + result.data!!.items
			}
			total = result.data!!.total
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
}