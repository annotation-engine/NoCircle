package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Users : NoTable("tb_user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
	
	val pinyin = varchar("pinyin", 100)
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
}

class User(id: EntityID<Int>) : NoIntEntity<Users>(id, Users) {
	
	companion object : IntEntityClass<User>(Users)
	
	var username by table.username
	
	var password by table.password
	
	val nickname by table.nickname
	
	val pinyin by table.pinyin
	
	val avatarUrl by table.avatarUrl
}