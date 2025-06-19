package com.nocircle.app.pages.main.person.message

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.FriendRequestType
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.coroutines.FunctionLocker
import com.nocircle.common.websocket.WebSocketScheduler
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.websocket.WebSocketType
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageCenterViewModel : NoViewModel() {
	
	private val _sentRequests = MutableStateFlow(emptyList<FriendRequestDTO>())
	val sentRequests = _sentRequests.asStateFlow()
	
	private val _receivedRequests = MutableStateFlow(emptyList<FriendRequestDTO>())
	val receivedRequests = _receivedRequests.asStateFlow()
	
	init {
		viewModelScope.launch {
			async { loadSentRequests() }
			async { loadReceivedRequests() }
			
			WebSocketScheduler.addCollect(WebSocketType.FRIEND_SENT_REQUEST) {
				loadSentRequests()
			}
			
			WebSocketScheduler.addCollect(WebSocketType.FRIEND_RECEIVED_REQUEST) {
				loadReceivedRequests()
			}
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
	
	suspend fun cancelSentRequest(id: Int, targetId: Int) {
		FunctionLocker.tryWithLock(::cancelSentRequest) {
			val result = ktorfitx.friendApi.cancelRequest(id, targetId)
				?: return@tryWithLock networkError()
			if (result.success) {
				loadSentRequests()
			}
			autoShowNoSnackbar(result.success, result.msg)
		}
	}
	
	suspend fun deleteSentRequest(id: Int, targetId: Int) {
		FunctionLocker.tryWithLock(::deleteSentRequest) {
			val result = ktorfitx.friendApi.deleteRequest(id, targetId)
				?: return@tryWithLock networkError()
			if (result.success) {
				loadSentRequests()
			}
			autoShowNoSnackbar(result.success, result.msg)
		}
	}
	
	suspend fun rejectReceivedRequest(id: Int, targetId: Int) {
		FunctionLocker.tryWithLock(::rejectReceivedRequest) {
			val result = ktorfitx.friendApi.rejectRequest(id, targetId)
				?: return@tryWithLock networkError()
			if (result.success) {
				loadReceivedRequests()
			}
			autoShowNoSnackbar(result.success, result.msg)
		}
	}
	
	suspend fun agreeReceivedRequest(id: Int, targetId: Int) {
		FunctionLocker.tryWithLock(::agreeReceivedRequest) {
			val result = ktorfitx.friendApi.agreeRequest(id, targetId)
				?: return@tryWithLock networkError()
			if (result.success) {
				loadReceivedRequests()
			}
			autoShowNoSnackbar(result.success, result.msg)
		}
	}
	
	override fun onCleared() {
		super.onCleared()
		WebSocketScheduler.removeCollects(
			WebSocketType.FRIEND_SENT_REQUEST,
			WebSocketType.FRIEND_RECEIVED_REQUEST
		)
	}
}