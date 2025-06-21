package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.annotation.GET
import cn.vividcode.multiplatform.ktorfitx.annotation.Query
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.PageResult
import com.nocircle.shared.model.friend.FriendDTO

@Api("friend")
interface FriendApi {
	
	@BearerAuth
	@GET("query")
	suspend fun queryFriendList(
		@Query pageNumber: Int,
		@Query pageSize: Int,
		@Query orderType: FriendOrderType
	): ResultBody<PageResult<FriendDTO>>
}

enum class FriendRequestType {
	SENT,
	RECEIVED
}

enum class FriendOrderType {
	PINYIN_ASC,
	PINYIN_DESC,
	CREATE_TIME_ASC,
	CREATE_TIME_DESC
}