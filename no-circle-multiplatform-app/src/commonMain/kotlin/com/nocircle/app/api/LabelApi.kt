package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addLabel(
		@Form label: String,
		@Form color: Int,
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteLabelById(
		@Form id: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("update")
	suspend fun updateLabel(
		@Form id: Int,
		@Form label: String,
		@Form color: Int,
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabels(): ResultBody<List<LabelVO>>?
}

@Serializable
data class LabelVO(
	val id: Int,
	val label: String,
	val color: Int,
)