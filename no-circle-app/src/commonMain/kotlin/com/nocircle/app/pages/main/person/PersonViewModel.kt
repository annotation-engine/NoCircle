package com.nocircle.app.pages.main.person

import androidx.lifecycle.viewModelScope
import com.nocircle.app.http.ktorClient
import com.nocircle.common.expends.safeGet
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetail?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadUserDetail()
		}
	}
	
	suspend fun loadUserDetail() {
		val result = ktorClient.safeGet<UserDetail>("user/detail") ?: return
		if (result.success) {
			_userDetail.value = result.data!!
		}
	}
	
	@Serializable
	data class UserDetail(
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val labels: List<Pair<String, String>>
	)
}