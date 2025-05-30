package com.nocircle.server.app.tables.user

import com.nocircle.server.common.exposed.BaseTable
import com.nocircle.server.common.exposed.logicDeleteWhere
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.logicUpdate
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserLabels : BaseTable("tb_user_label") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 16)
	
	val color = integer("color")
	
	fun getById(id: Int): UserLabel? {
		val row = UserLabels.selectAll()
			.where { UserLabels.id eq id }
			.logicExists(UserLabels)
			.singleOrNull() ?: return null
		return UserLabel.wrapRow(row)
	}
	
	fun getListByUserId(userId: Int): List<UserLabel> {
		val query = UserLabels.selectAll()
			.where { UserLabels.userId eq userId }
			.logicExists(UserLabels)
		return UserLabel.wrapRows(query).toList()
	}
	
	fun getListByUserIdAndNotId(userId: Int, id: Int): List<UserLabel> {
		val query = UserLabels.selectAll()
			.where { (UserLabels.userId eq userId) and (UserLabels.id neq id) }
			.logicExists(UserLabels)
		return UserLabel.wrapRows(query).toList()
	}
	
	fun insert(userId: Int, label: String, color: Int): Boolean {
		val insert = UserLabels.insert {
			it[this.userId] = userId
			it[this.label] = label
			it[this.color] = color
		}
		return insert.insertedCount == 1
	}
	
	fun update(userId: Int, id: Int, label: String, color: Int): Boolean {
		val count = UserLabels.logicUpdate(
			where = { (UserLabels.id eq id) and (UserLabels.userId eq userId) }
		) {
			it[this.label] = label
			it[this.color] = color
		}
		return count == 1
	}
	
	fun delete(userId: Int, id: Int): Boolean {
		val count = UserLabels.logicDeleteWhere {
			(UserLabels.id eq id) and (UserLabels.userId eq userId)
		}
		return count == 1
	}
}

class UserLabel(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<UserLabel>(UserLabels)
	
	var label by UserLabels.label
	
	var color by UserLabels.color
}