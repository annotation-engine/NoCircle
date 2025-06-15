package com.nocircle.app.pages.main.person.message

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.FriendRequestType
import com.nocircle.app.api.RequestDTO
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageCenterViewModel : NoViewModel() {
	
	private val _sentRequests = MutableStateFlow(emptyList<RequestDTO>())
	val sentRequests = _sentRequests.asStateFlow()
	
	private val _receivedRequests = MutableStateFlow(emptyList<RequestDTO>())
	val receivedRequests = _receivedRequests.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadSentRequests()
			loadReceivedRequests()
		}
	}
	
	suspend fun loadSentRequests() {
		val result = ktorfitx.friendApi.queryRequest(FriendRequestType.SENT) ?: return networkError()
		if (result.success) {
			_sentRequests.value = result.data!!
		} else {
			return showNoSnackbar(result.msg)
		}
	}
	
	suspend fun loadReceivedRequests() {
		val result = ktorfitx.friendApi.queryRequest(FriendRequestType.RECEIVED) ?: return networkError()
		if (result.success) {
			_receivedRequests.value = result.data!!
		} else {
			return showNoSnackbar(result.msg)
		}
	}
	
	suspend fun cancelSentRequest(requestId: Int) {
		val result = ktorfitx.friendApi.cancelRequest(requestId) ?: return networkError()
		if (result.success) {
			loadSentRequests()
		} else {
			showNoSnackbar(result.msg)
		}
	}
	
	suspend fun deleteSentRequest(requestId: Int) {
		val result = ktorfitx.friendApi.deleteRequest(requestId) ?: return networkError()
		if (result.success) {
			loadSentRequests()
		}
		autoShowNoSnackbar(result.success, result.msg)
	}
}