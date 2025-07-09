package com.nocircle.app.pages.person

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendRequestApi
import com.nocircle.app.api.impls.labelApi
import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.expends.success
import com.nocircle.common.log.NoLog
import com.nocircle.common.websocket.WebSocketScheduler
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.model.user.UserDetailDTO
import com.nocircle.shared.websocket.WebSocketType
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetailDTO?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	private val _labels = MutableStateFlow<List<UserLabelDTO>>(emptyList())
	val labels = _labels.asStateFlow()
	
	private val _friendRequestPendingCount = MutableStateFlow(0)
	val friendRequestPendingCount = _friendRequestPendingCount.asStateFlow()
	
	init {
		viewModelScope.launch {
			WebSocketScheduler.addCollect(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT) {
				loadRequestReceivedCount()
			}
			
			async { loadUserDetail() }
			async { loadLabels() }
			async { loadRequestReceivedCount() }
		}
	}
	
	private suspend fun loadUserDetail(): Boolean {
		val result = ktorfitx.userApi.queryDetail()
			.getOrNull() ?: return networkError()
		NoLog.info(result)
		if (result.success) {
			_userDetail.value = result.data!!
		}
		return result.success
	}
	
	suspend fun loadLabels() {
		val result = ktorfitx.labelApi.queryLabelList()
			.getOrNull() ?: return
		if (result.success) {
			_labels.value = result.data!!
		}
	}
	
	suspend fun loadRequestReceivedCount() {
		val result = ktorfitx.friendRequestApi.queryPendingRequestCount()
			.getOrNull() ?: return
		if (result.success) {
			_friendRequestPendingCount.value = result.data!!
		}
	}
	
	override fun onCleared() {
		super.onCleared()
		WebSocketScheduler.removeCollects(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT)
	}
}