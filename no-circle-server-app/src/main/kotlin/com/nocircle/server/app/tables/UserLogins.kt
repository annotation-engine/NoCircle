package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp

object UserLogins : NoTable("tb_user_login") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val loginTime = timestamp("login_time")
	
	val method = enumerationByName<Method>("method", 8)
	
	enum class Method { PASSWORD, TOKEN }
}

class UserLogin(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<UserLogin>(UserLogins)
	
	val userId by UserLogins.userId
	
	val loginTime by UserLogins.loginTime
	
	val method by UserLogins.method
}