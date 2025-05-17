package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.POST
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody

@Api(url = "auth")
interface AuthApi {
	
	@BearerAuth
	@POST("verifyToken")
	suspend fun verifyToken(): ResultBody<Boolean>?
}