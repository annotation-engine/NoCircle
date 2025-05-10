package com.nocircle.app.pages.main.person

import androidx.lifecycle.viewModelScope
import com.nocircle.app.http.ktorClient
import com.nocircle.common.ktor.safeGet
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetail?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	private val _userInformation = MutableStateFlow<UserInformation?>(null)
	val userInformation = _userInformation.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadUserDetail()
			loadUserInformation()
		}
	}
	
	private suspend fun loadUserDetail() {
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
	
	private suspend fun loadUserInformation() {
		val result = ktorClient.safeGet<UserInformation>("user/information") ?: return
		if (result.success) {
			_userInformation.value = result.data!!
		}
	}
	
	@Serializable
	data class UserInformation(
		val friendCount: Int,
		val groupCount: Int,
		val messageCount: Int
	)
}