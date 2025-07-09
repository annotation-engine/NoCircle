package com.nocircle.app.pages.friends.list.add

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendRequestApi
import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.coroutines.FunctionLocker
import com.nocircle.common.coroutines.OnBusyReturnFalse
import com.nocircle.common.expends.success
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.user.UserSearchDTO
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
	
	private val _result = MutableStateFlow<UserSearchDTO?>(null)
	val result = _result.asStateFlow()
	
	init {
		viewModelScope.launch {
			search
				.debounce(0.5.seconds)
				.collectLatest(::searchUserByUsername)
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
		return FunctionLocker.tryWithLock(::sendFriendAddRequest, OnBusyReturnFalse) {
			val result = ktorfitx.friendRequestApi.addRequest(receiverId)
				.getOrNull() ?: return@tryWithLock networkError()
			autoShowNoSnackbar(result.success, result.msg)
			result.success
		}
	}
	
	private suspend fun searchUserByUsername(username: String) {
		if (username.isBlank()) {
			_result.value = null
			return
		}
		val result = ktorfitx.userApi.searchUser(username)
			.getOrNull() ?: return networkError()
		if (result.success) {
			_result.value = result.data
		} else {
			_result.value = null
		}
	}
}