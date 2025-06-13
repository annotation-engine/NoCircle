package com.nocircle.app.api

import androidx.compose.runtime.Immutable
import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addLabel(
		@Field label: String,
		@Field color: Int,
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteLabelById(
		@Field id: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("update")
	suspend fun updateLabel(
		@Field id: Int,
		@Field label: String,
		@Field color: Int,
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabels(): ResultBody<List<LabelVO>>?
}

@Immutable
@Serializable
data class LabelVO(
	val id: Int,
	val label: String,
	val color: Int,
)