package com.nocircle.server.app.tables.user

import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.exposed.BaseIntEntity
import com.nocircle.server.common.exposed.BaseTable
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object Users : BaseTable("tb_user") {
	
	val username = varchar("username", 20)
	
	val password = char("password", 98)
	
	val nickname = varchar("nickname", 20)
		.nullable()
	
	val avatarUrl = varchar("avatar_url", 255)
		.nullable()
	
	fun insert(username: String, password: String): Boolean {
		val insert = Users.insert {
			it[this.username] = username
			it[this.password] = PasswordUtils.encrypt(password)
		}
		return insert.insertedCount == 1
	}
	
	fun getById(id: Int): User? {
		val row = Users.selectAll()
			.where { Users.id eq id }
			.logicExists(Users)
			.singleOrNull() ?: return null
		return User.wrapRow(row)
	}
	
	fun getByUsername(username: String): User? {
		val row = Users.selectAll()
			.where { Users.username eq username }
			.logicExists(Users)
			.singleOrNull() ?: return null
		return User.wrapRow(row)
	}
	
	fun isExistsByUsername(username: String): Boolean {
		return Users.selectAll()
			.where { Users.username eq username }
			.logicExists(Users)
			.exists()
	}
}

class User(id: EntityID<Int>) : BaseIntEntity(id, Users) {
	
	companion object : IntEntityClass<User>(Users)
	
	var username by Users.username
	
	var password by Users.password
	
	val nickname by Users.nickname
	
	val avatarUrl by Users.avatarUrl
}