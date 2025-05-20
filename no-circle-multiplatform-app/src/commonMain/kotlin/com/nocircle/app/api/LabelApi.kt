package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.GET
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabels(): ResultBody<List<LabelVO>>?
}

@Serializable
data class LabelVO(
	val id: Int,
	val label: String,
	val color: String,
)