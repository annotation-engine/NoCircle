package com.nocircle.server.services.user

import com.nocircle.server.models.ApiResult
import com.nocircle.server.plugins.userPrincipal
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
import com.nocircle.server.services.noParameters
import com.nocircle.server.tables.isLogicExists
import com.nocircle.server.tables.user.User
import com.nocircle.server.tables.user.UserLabel
import com.nocircle.server.tables.user.UserLabels
import com.nocircle.server.tables.user.Users
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserDetailService : NoService<UserDetailService.UserDetail> {
	
	override val path = "/user/detail"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val userId = call.userPrincipal.userId
		this["userId"] = userId
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<UserDetail> {
		val userId: Int by parameters
		val data = transaction {
			val userRow = Users.selectAll()
				.where { Users.id eq userId and Users.isLogicExists }
				.firstOrNull() ?: return@transaction null
			val user = User.wrapRow(userRow)
			val userLabelRow = UserLabels.selectAll()
				.where { UserLabels.userId eq userId and UserLabels.isLogicExists }
			val userLabels = UserLabel.wrapRows(userLabelRow).map {
				it.label to it.color
			}
			UserDetail(
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				labels = userLabels
			)
		} ?: return ApiResult.failure("用户详情查询成功")
		return ApiResult.success(data, "用户详情查询成功")
	}
	
	@Serializable
	data class UserDetail(
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val labels: List<Pair<String, String>>
	)
}