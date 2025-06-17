package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.friend.FriendSearchDTO
import com.nocircle.shared.model.friend.request.FriendRequestDTO

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("search")
	suspend fun search(
		@Query username: String
	): ResultBody<FriendSearchDTO>?
	
	@BearerAuth
	@POST("request/add")
	suspend fun addRequest(
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("request/query")
	suspend fun queryRequest(
		@Query type: FriendRequestType
	): ResultBody<List<FriendRequestDTO>>?
	
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

enum class FriendRequestType {
	SENT,
	RECEIVED
}