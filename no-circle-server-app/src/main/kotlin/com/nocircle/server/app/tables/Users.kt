package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Users : NoTable("user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
	
	val pinyin = varchar("pinyin", 100)
}

class User(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<User>(Users)
	
	val username by Users.username
	
	val password by Users.password
	
	val nickname by Users.nickname
	
	val pinyin by Users.pinyin
}