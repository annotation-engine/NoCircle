package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.label.LabelDTO

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addLabel(
		@Field label: String,
		@Field color: String,
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteLabel(
		@Field id: Int
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("update")
	suspend fun updateLabel(
		@Field id: Int,
		@Field label: String,
		@Field color: String,
	): ResultBody<Unit>?
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabelList(): ResultBody<List<LabelDTO>>?
}