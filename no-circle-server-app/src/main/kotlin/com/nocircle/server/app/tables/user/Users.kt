package com.nocircle.server.app.tables.user

import com.nocircle.server.common.tables.BaseTable
import com.nocircle.server.common.tables.IntEntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Users : BaseTable("tb_user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
		.nullable()
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
}

class User(id: IntEntityID) : IntEntity(id) {
	
	companion object : IntEntityClass<User>(Users)
	
	var username by Users.username
	
	var password by Users.password
	
	val nickname by Users.nickname
	
	val avatarUrl by Users.avatarUrl
}