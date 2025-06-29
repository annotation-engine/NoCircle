package com.nocircle.shared.model.friend

import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.model.user.Gender
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
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
	@Contextual
	val createTime: Instant
)