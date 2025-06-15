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
			loadFriendRequests()
		}
	}
	
	suspend fun loadFriendRequests() {
		val sentResult = ktorfitx.friendApi.queryRequest(FriendRequestType.SENT) ?: return networkError()
		if (sentResult.success) {
			_sentRequests.value = sentResult.data!!
		} else {
			return showNoSnackbar(sentResult.msg)
		}
		val receivedResult = ktorfitx.friendApi.queryRequest(FriendRequestType.RECEIVED) ?: return networkError()
		if (receivedResult.success) {
			_receivedRequests.value = receivedResult.data!!
		} else {
			return showNoSnackbar(receivedResult.msg)
		}
	}
}