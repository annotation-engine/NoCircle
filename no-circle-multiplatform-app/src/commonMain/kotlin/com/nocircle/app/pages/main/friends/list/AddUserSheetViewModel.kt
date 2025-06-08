package com.nocircle.app.pages.main.friends.list

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.SearchUserVO
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class AddUserSheetViewModel : NoViewModel() {
	
	private val _username = MutableStateFlow("")
	val username = _username.asStateFlow()
	
	private val _result = MutableStateFlow<SearchUserVO?>(null)
	val result = _result.asStateFlow()
	
	init {
		viewModelScope.launch {
			_username.debounce(500.milliseconds)
				.distinctUntilChanged()
				.collect(::getFriendByUsernameOrNickname)
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_username.value = search
		}
	}
	
	private suspend fun getFriendByUsernameOrNickname(username: String) {
		if (username.isBlank()) {
			_result.value = null
			return
		}
		val result = ktorfitx.userApi.queryUserByUsername(username) ?: return networkError()
		if (result.success) {
			_result.value = result.data
		} else {
			showNoErrorSnackbar(result.msg)
		}
	}
}