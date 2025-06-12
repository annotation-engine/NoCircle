package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.Form
import cn.vividcode.multiplatform.ktorfitx.annotation.POST
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@POST("addRequest")
	suspend fun sendAddRequest(
		@Form receiverId: Int
	): ResultBody<Unit>?
}