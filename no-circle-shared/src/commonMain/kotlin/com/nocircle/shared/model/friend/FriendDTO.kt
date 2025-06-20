package com.nocircle.shared.model.friend

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
	val userId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val pinyin: String,
	val createdTime: LocalDateTime
)