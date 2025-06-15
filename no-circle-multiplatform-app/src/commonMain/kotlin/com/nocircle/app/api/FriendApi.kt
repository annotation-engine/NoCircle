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
	suspend fun addRequest(
		@Field receiverId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("request/query")
	suspend fun queryRequest(): ResultBody<FriendRequestDTO>?
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
	val sentRequests: List<RequestDTO>,
	val receivedRequests: List<RequestDTO>
) {
	
	@Immutable
	@Serializable
	data class RequestDTO(
		val id: Int,
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val status: RequestStatus,
		val labels: List<LabelDTO>,
		val updateTime: String
	)
	
	@Serializable
	enum class RequestStatus { AGREED, REJECTED, WAITING, CANCELED }
	
	@Immutable
	@Serializable
	data class LabelDTO(
		val label: String,
		val color: String,
	)
}