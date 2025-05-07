package com.nocircle.server.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

object UserTable : BaseTable("tb_user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
		.nullable()
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
}

class User(id: IntEntityID) : IntEntity(id) {
	
	companion object : IntEntityClass<User>(UserTable)
	
	var username by UserTable.username
	
	var password by UserTable.password
	
	val nickname by UserTable.nickname
	
	val avatarUrl by UserTable.avatarUrl
}