package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.Api
import cn.ktorfitx.multiplatform.annotation.BearerAuth
import cn.ktorfitx.multiplatform.annotation.GET
import cn.ktorfitx.multiplatform.annotation.Query
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.FriendDTO
import com.nocircle.shared.model.friend.FriendDetailDTO

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryFriendList(): Result<ApiResult<List<FriendDTO>>>
	
	@BearerAuth
	@GET("version/query")
	suspend fun queryFriendVersion(): Result<ApiResult<Int>>
	
	@BearerAuth
	@GET("detail/query")
	suspend fun queryFriendDetail(
		@Query friendId: Int,
	): Result<ApiResult<FriendDetailDTO>>
}

enum class FriendRequestType {
	SENT,
	RECEIVED
}