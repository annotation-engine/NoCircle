package com.nocircle.shared.model.friend.request

import com.nocircle.shared.model.label.UserLabelDTO
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestDTO(
	val id: Int,
	val targetId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val status: Status,
	val createTime: String,
	val labels: List<UserLabelDTO>
) {
	
	@Serializable
	enum class Status {
		AGREED,
		REJECTED,
		PENDING,
		CANCELED
	}
}