package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.Api
import cn.ktorfitx.multiplatform.annotation.BearerAuth
import cn.ktorfitx.multiplatform.annotation.POST
import cn.ktorfitx.multiplatform.core.model.ApiResult

@Api(url = "auth")
interface AuthApi {
	
	@BearerAuth
	@POST("verifyToken")
	suspend fun verifyToken(): ApiResult<Boolean>?
}