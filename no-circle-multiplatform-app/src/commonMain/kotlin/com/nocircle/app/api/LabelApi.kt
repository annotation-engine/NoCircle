package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabels(): ResultBody<List<LabelVO>>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteLabelById(
		@Form id: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("add")
	suspend fun addLabel(
		@Form label: String,
		@Form color: String,
	): ResultBody<Unit>?
}

@Serializable
data class LabelVO(
	val id: Int,
	val label: String,
	val color: String,
)