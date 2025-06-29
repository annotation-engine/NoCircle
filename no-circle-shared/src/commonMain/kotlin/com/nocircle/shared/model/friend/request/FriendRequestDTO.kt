package com.nocircle.shared.model.friend.request

import com.nocircle.shared.model.label.UserLabelDTO
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FriendRequestDTO(
	val id: Int,
	val targetId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val status: Status,
	@Contextual
	val createTime: Instant,
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