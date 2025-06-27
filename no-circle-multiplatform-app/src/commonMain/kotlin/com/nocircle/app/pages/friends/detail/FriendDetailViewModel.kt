package com.nocircle.app.pages.friends.detail

import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendDetailDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendDetailViewModel : NoViewModel() {
	
	private val _friendDetail = MutableStateFlow<FriendDetailDTO?>(null)
	val friendDetail = _friendDetail.asStateFlow()
	
	suspend fun loadFriendDetail(friendId: Int) {
		val result = ktorfitx.friendApi.queryFriendDetail(friendId)
			?: return networkError()
		if (result.success) {
			_friendDetail.value = result.data
		} else {
			showNoErrorSnackbar(result.msg)
		}
	}
}