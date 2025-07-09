package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.*
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.request.FriendRequestDTO

@Api("/friend/request")
interface FriendRequestApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addRequest(
		@Field targetId: Int
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@POST("cancel")
	suspend fun cancelRequest(
		@Field id: Int,
		@Field targetId: Int
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteRequest(
		@Field targetId: Int
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@POST("reject")
	suspend fun rejectRequest(
		@Field id: Int,
		@Field targetId: Int
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@POST("agree")
	suspend fun agreeRequest(
		@Field id: Int,
		@Field targetId: Int
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@GET("query")
	suspend fun queryRequestList(
		@Query type: FriendRequestType
	): Result<ApiResult<List<FriendRequestDTO>>>
	
	@BearerAuth
	@GET("queryPendingCount")
	suspend fun queryPendingRequestCount(): Result<ApiResult<Int>>
}