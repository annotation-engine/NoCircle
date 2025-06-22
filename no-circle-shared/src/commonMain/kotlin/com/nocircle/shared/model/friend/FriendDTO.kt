package com.nocircle.shared.model.friend

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
	val friendId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val pinyin: String,
	val createTime: LocalDateTime
) {
	val initial by lazy { pinyin.first().uppercase() }
}