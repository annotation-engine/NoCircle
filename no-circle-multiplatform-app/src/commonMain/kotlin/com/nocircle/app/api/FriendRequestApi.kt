package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.friend.request.FriendRequestDTO

@Api("/friend/request")
interface FriendRequestApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addRequest(
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("cancel")
	suspend fun cancelRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("reject")
	suspend fun rejectRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("agree")
	suspend fun agreeRequest(
		@Field id: Int,
		@Field targetId: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("query")
	suspend fun queryRequestList(
		@Query type: FriendRequestType
	): ResultBody<List<FriendRequestDTO>>
	
	@BearerAuth
	@GET("queryPendingCount")
	suspend fun queryPendingRequestCount(): ResultBody<Int>?
}