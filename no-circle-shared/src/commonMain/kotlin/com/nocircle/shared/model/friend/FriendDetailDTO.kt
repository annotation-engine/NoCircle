package com.nocircle.shared.model.friend

import com.nocircle.shared.model.label.UserLabelDTO
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FriendDetailDTO(
	val friendId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val labels: List<UserLabelDTO>,
	val createTime: LocalDateTime
)