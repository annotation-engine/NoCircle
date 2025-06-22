package com.nocircle.shared.model.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
	val friendId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val pinyin: String,
)