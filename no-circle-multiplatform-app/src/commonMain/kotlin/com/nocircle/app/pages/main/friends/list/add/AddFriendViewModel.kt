package com.nocircle.app.pages.main.friends.list.add

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.coroutines.KFunctionLocker
import com.nocircle.common.coroutines.OnBusyReturnFalse
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendSearchDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
class AddFriendViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	private val _result = MutableStateFlow<FriendSearchDTO?>(null)
	val result = _result.asStateFlow()
	
	init {
		viewModelScope.launch {
			search
				.debounce(0.5.seconds)
				.collectLatest(::searchFriendByUsername)
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
	
	fun init() {
		_search.value = ""
		_result.value = null
	}
	
	suspend fun sendFriendAddRequest(receiverId: Int): Boolean {
		return KFunctionLocker.tryWithLock(::sendFriendAddRequest, OnBusyReturnFalse) {
			val result = ktorfitx.friendApi.addRequest(receiverId)
				?: return@tryWithLock networkError()
			autoShowNoSnackbar(result.success, result.msg)
			result.success
		}
	}
	
	private suspend fun searchFriendByUsername(username: String) {
		if (username.isBlank()) {
			_result.value = null
			return
		}
		val result = ktorfitx.friendApi.search(username)
			?: return networkError()
		if (result.success) {
			_result.value = result.data
		} else {
			_result.value = null
		}
	}
}