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
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("request/query")
	suspend fun queryRequest(
		@Query type: FriendRequestType
	): ResultBody<List<RequestDTO>>?
	
	@BearerAuth
	@POST("request/cancel")
	suspend fun cancelRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("request/delete")
	suspend fun deleteRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("request/reject")
	suspend fun rejectRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	
	@BearerAuth
	@POST("request/agree")
	suspend fun agreeRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("request/queryWaitingCount")
	suspend fun queryWaitingRequestCount(): ResultBody<Int>?
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

enum class FriendRequestType {
	SENT,
	RECEIVED
}

@Immutable
@Serializable
data class RequestDTO(
	val id: Int,
	val targetId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val status: RequestStatus,
	val createTime: String,
	val labels: Map<String, String>
) {
	
	@Serializable
	enum class RequestStatus { AGREED, REJECTED, WAITING, CANCELED }
}