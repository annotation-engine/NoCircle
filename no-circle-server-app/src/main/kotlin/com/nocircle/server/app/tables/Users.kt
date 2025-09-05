package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoEntity
import com.nocircle.server.common.exposed.NoEntityClass
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object Users : NoTable("user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
	
	val pinyin = varchar("pinyin", 100)
}

class User(id: EntityID<Int>) : NoEntity(id) {
	
	companion object : NoEntityClass<User>(Users, ::User)
	
	val username by Users.username
	
	val password by Users.password
	
	val nickname by Users.nickname
	
	val pinyin by Users.pinyin
}