package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.User
import com.nocircle.server.app.tables.Users
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.expends.toPinyin
import com.nocircle.server.common.exposed.exists
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.selectWithout
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserDao {
	
	
	fun insertOne(username: String, password: String, nickname: String): Boolean {
		val insert = Users.insert {
			it[this.username] = username
			it[this.password] = PasswordUtils.encrypt(password)
			it[this.nickname] = nickname
			it[this.pinyin] = nickname.toPinyin("")
		}
		return insert.insertedCount == 1
	}
	
	fun getOneById(id: Int): User? {
		val row = Users.selectWithout(Users.password)
			.where { Users.id eq id }
			.logicExists(Users)
			.singleOrNull() ?: return null
		return User.wrapRow(row)
	}
	
	fun getListByIds(ids: Collection<Int>): List<User> {
		if (ids.isEmpty()) return emptyList()
		val query = Users.selectWithout(Users.password)
			.where { Users.id inList ids }
			.logicExists(Users)
		return User.wrapRows(query).toList()
	}
	
	fun getOneByUsername(username: String): User? {
		val row = Users.selectAll()
			.where { Users.username eq username }
			.logicExists(Users)
			.singleOrNull() ?: return null
		return User.wrapRow(row)
	}
	
	fun getPinyinById(id: Int): String? {
		val row = Users.select(Users.pinyin)
			.where { Users.id eq id }
			.logicExists(Users)
			.singleOrNull() ?: return null
		return row[Users.pinyin]
	}
	
	fun isExistsByUsername(username: String): Boolean {
		return Users.select(Users.id)
			.where { Users.username eq username }
			.logicExists(Users)
			.exists()
	}
	
	fun isExistsByUserId(userId: Int): Boolean {
		return Users.select(Users.id)
			.where { Users.id eq userId }
			.logicExists(Users)
			.exists()
	}
}