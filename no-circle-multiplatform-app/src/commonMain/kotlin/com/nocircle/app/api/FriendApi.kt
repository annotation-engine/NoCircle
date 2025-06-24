package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.GET
import cn.vividcode.multiplatform.ktorfitx.annotation.Query
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.friend.FriendDTO
import com.nocircle.shared.model.friend.FriendDetailDTO

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryFriendList(): ResultBody<List<FriendDTO>>?
	
	@BearerAuth
	@GET("version/query")
	suspend fun queryFriendVersion(): ResultBody<Int>?
	
	@BearerAuth
	@GET("detail/query")
	suspend fun queryFriendDetail(
		@Query friendId: Int,
	): ResultBody<FriendDetailDTO>?
}

enum class FriendRequestType {
	SENT,
	RECEIVED
}