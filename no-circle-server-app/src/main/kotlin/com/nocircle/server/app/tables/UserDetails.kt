package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoEntity
import com.nocircle.server.common.exposed.NoEntityClass
import com.nocircle.server.common.exposed.NoTable
import com.nocircle.shared.model.user.Gender
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object UserDetails : NoTable("user_detail") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
		.default(null)
	
	val email = varchar("email", 255)
		.nullable()
		.default(null)
	
	val signature = varchar("signature", 20)
		.nullable()
		.default(null)
	
	val gender = enumerationByName<Gender>("gender", 18)
		.nullable()
		.default(null)
}

class UserDetail(id: EntityID<Int>) : NoEntity(id) {
	
	companion object : NoEntityClass<UserDetail>(UserDetails, ::UserDetail)
	
	val userId by UserDetails.userId
	
	val avatarUrl by UserDetails.avatarUrl
	
	val email by UserDetails.email
	
	val signature by UserDetails.signature
	
	val gender by UserDetails.gender
}