package com.nocircle.shared.model.friend

import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.serialization.ISOInstantSerializer
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
	val labels: List<UserLabelDTO>,
	@Serializable(with = ISOInstantSerializer::class)
	val createTime: Instant
)