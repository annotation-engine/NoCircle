package com.nocircle.server.app.services.user

import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.expends.format
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object UserDetailService : NoService<UserDetailService.UserDetail> {

    override val path = "/user/detail"

    override val method = HttpMethod.Get

    override val auth = true

    override suspend fun process(parameters: NoParameters): ApiResult<UserDetail> {
        val userId: Int by parameters
        val data = transaction {
            val user = Users.getById(userId) ?: return@transaction null
            val lastLoginTime = UserLogins.getLastLoginByUserId(userId)?.loginTime
            UserDetail(
                username = user.username,
                nickname = user.nickname,
                avatarUrl = user.avatarUrl,
                lastLoginTime = lastLoginTime?.format()
            )
        } ?: return ApiResult.failure("用户详情查询成功")
        return ApiResult.success(data, "用户详情查询成功")
    }

    @Serializable
    data class UserDetail(
        val username: String,
        val nickname: String?,
        val avatarUrl: String?,
        val lastLoginTime: String?
    )
}