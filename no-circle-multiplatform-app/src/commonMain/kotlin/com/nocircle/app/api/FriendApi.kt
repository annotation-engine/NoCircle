package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.Field
import cn.vividcode.multiplatform.ktorfitx.annotation.GET
import cn.vividcode.multiplatform.ktorfitx.annotation.POST
import cn.vividcode.multiplatform.ktorfitx.annotation.Query
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("search")
	suspend fun searchByUsername(
		@Query username: String
	): ResultBody<SearchUserVO>?
	
	@BearerAuth
	@POST("addRequest")
	suspend fun sendAddRequest(
		@Field receiverId: Int
	): ResultBody<Unit>?
	
}