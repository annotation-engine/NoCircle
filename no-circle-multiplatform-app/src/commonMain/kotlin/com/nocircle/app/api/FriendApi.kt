package com.nocircle.app.api

import androidx.compose.runtime.Immutable
import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("search")
	suspend fun search(
		@Query username: String
	): ResultBody<SearchUserDTO>?
	
	@BearerAuth
	@POST("request/add")
	suspend fun requestAdd(
		@Field receiverId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("request/query")
	suspend fun requestQuery(): ResultBody<FriendRequestDTO>
}

@Immutable
@Serializable
data class SearchUserDTO(
	val userId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val labels: List<LabelDTO>,
	val relationship: RelationshipDTO,
	val isAlreadySend: Boolean
) {
	
	@Serializable
	enum class RelationshipDTO {
		FRIEND,
		OWNER,
		STRANGER
	}
}

@Immutable
@Serializable
data class FriendRequestDTO(
	val sentRequests: List<SentRequestDTO>,
	val receivedRequests: List<ReceivedRequestDTO>
) {
	
	@Immutable
	@Serializable
	data class SentRequestDTO(
		val receiverId: Int,
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val status: String,
		val updateTime: String
	)
	
	@Immutable
	@Serializable
	data class ReceivedRequestDTO(
		val senderId: Int,
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val status: String,
		val updateTime: String
	)
}