package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.User
import com.nocircle.server.app.tables.Users
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.expends.toPinyin
import com.nocircle.server.common.exposed.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserDao {
	
	fun insertOne(username: String, password: String, nickname: String): Int? {
		return Users.insertReturning(
			returning = listOf(Users.id)
		) {
			it[this.username] = username
			it[this.password] = PasswordUtils.encrypt(password)
			it[this.nickname] = nickname
			it[this.pinyin] = nickname.toPinyin("").replace(" ", "")
		}
			.singleOrNull()
			?.wrapRow(User)
			?.id?.value
	}
	
	fun getOneById(id: Int): User? {
		return Users.selectWithout(Users.password)
			.where { Users.id eq id }
			.logicExists(Users)
			.singleOrNull()
			?.wrapRow(User)
	}
	
	fun getMapByIds(ids: Collection<Int>): Map<Int, User> {
		if (ids.isEmpty()) return emptyMap()
		return Users.selectWithout(Users.password)
			.where { Users.id inList ids }
			.logicExists(Users)
			.wrapRows(User)
			.associateBy { it.id.value }
	}
	
	fun getOneByUsername(username: String): User? {
		return Users.selectAll()
			.where { Users.username eq username }
			.logicExists(Users)
			.singleOrNull()
			?.wrapRow(User)
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