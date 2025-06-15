package com.nocircle.app.pages.main.person.message

import com.nocircle.app.api.FriendRequestDTO
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.log.NoLog
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageCenterViewModel : NoViewModel() {
	
	private val _sentRequests = MutableStateFlow(emptyList<FriendRequestDTO.RequestDTO>())
	val sentRequests = _sentRequests.asStateFlow()
	
	private val _receivedRequests = MutableStateFlow(emptyList<FriendRequestDTO.RequestDTO>())
	val receivedRequests = _receivedRequests.asStateFlow()
	
	suspend fun loadFriendRequest() {
		val result = ktorfitx.friendApi.queryRequest() ?: return networkError()
		NoLog.info(result)
		if (result.success) {
			_sentRequests.value = result.data!!.sentRequests
			_receivedRequests.value = result.data!!.receivedRequests
		} else {
			showNoSnackbar(result.msg)
		}
	}
}