package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Users : NoTable("tb_user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
	
	val pinyin = varchar("pinyin", 100)
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
}

class User(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<User>(Users)
	
	var username by Users.username
	
	var password by Users.password
	
	val nickname by Users.nickname
	
	val pinyin by Users.pinyin
	
	val avatarUrl by Users.avatarUrl
}