package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp

object UserLogins : NoTable("tb_user_login") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val loginTime = timestamp("login_time")
	
	val method = enumerationByName<Method>("method", 8)
	
	enum class Method { PASSWORD, TOKEN }
}

class UserLogin(id: EntityID<Int>) : NoIntEntity<UserLogins>(id, UserLogins) {
	
	companion object : IntEntityClass<UserLogin>(UserLogins)
	
	val userId by table.userId
	
	val loginTime by table.loginTime
	
	val method by table.method
}