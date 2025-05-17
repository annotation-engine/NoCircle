package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.Form
import cn.vividcode.multiplatform.ktorfitx.annotation.POST
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.app.ktorfit.NoCircleScope

@Api(url = "user", apiScope = NoCircleScope::class)
interface UserApi {
	
	@POST("login")
	suspend fun login(
		@Form username: String,
		@Form password: String
	): ResultBody<String>
}