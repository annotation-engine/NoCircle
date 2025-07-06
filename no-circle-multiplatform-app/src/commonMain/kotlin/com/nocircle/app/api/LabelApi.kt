package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.*
import cn.ktorfitx.multiplatform.core.model.ApiResult
import com.nocircle.shared.model.label.UserLabelDTO

@Api(url = "label")
interface LabelApi {
	
	@BearerAuth
	@POST("add")
	suspend fun addLabel(
		@Field label: String,
		@Field color: String,
	): ApiResult<Unit>?
	
	@BearerAuth
	@POST("delete")
	suspend fun deleteLabel(
		@Field id: Int
	): ApiResult<Unit>?
	
	@BearerAuth
	@POST("update")
	suspend fun updateLabel(
		@Field id: Int,
		@Field label: String,
		@Field color: String,
	): ApiResult<Unit>?
	
	@BearerAuth
	@GET("query")
	suspend fun queryLabelList(): ApiResult<List<UserLabelDTO>>?
}