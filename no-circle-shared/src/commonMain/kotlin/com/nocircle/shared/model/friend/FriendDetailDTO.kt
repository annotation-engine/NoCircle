package com.nocircle.shared.model.friend

import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.model.user.Gender
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class FriendDetailDTO(
	val friendId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val email: String?,
	val signature: String?,
	val gender: Gender?,
	val labels: List<UserLabelDTO>,
	val createTime: Instant
)