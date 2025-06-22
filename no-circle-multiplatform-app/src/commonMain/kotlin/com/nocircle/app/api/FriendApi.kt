package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.GET
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.friend.FriendDTO

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryFriendList(): ResultBody<List<FriendDTO>>?
	
	@BearerAuth
	@GET("queryVersion")
	suspend fun queryFriendVersion(): ResultBody<Int>?
}

enum class FriendRequestType {
	SENT,
	RECEIVED
}