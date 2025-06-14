package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.UserLabel
import com.nocircle.server.app.tables.UserLabels
import com.nocircle.server.common.exposed.logicDeleteWhere
import com.nocircle.server.common.exposed.logicExists
import com.nocircle.server.common.exposed.logicUpdate
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserLabelDao {
	
	fun getOneById(id: Int): UserLabel? {
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
	
	fun getListByUserIdAndNeqId(userId: Int, id: Int): List<UserLabel> {
		val query = UserLabels.selectAll()
			.where { (UserLabels.userId eq userId) and (UserLabels.id neq id) }
			.logicExists(UserLabels)
		return UserLabel.wrapRows(query).toList()
	}
	
	fun insertOne(userId: Int, label: String, color: String): Boolean {
		val insert = UserLabels.insert {
			it[this.userId] = userId
			it[this.label] = label
			it[this.color] = color
		}
		return insert.insertedCount == 1
	}
	
	fun updateOne(userId: Int, id: Int, label: String, color: String): Boolean {
		val count = UserLabels.logicUpdate(
			where = { (UserLabels.id eq id) and (UserLabels.userId eq userId) }
		) {
			it[this.label] = label
			it[this.color] = color
		}
		return count == 1
	}
	
	fun deleteOne(userId: Int, id: Int): Boolean {
		val count = UserLabels.logicDeleteWhere {
			(UserLabels.id eq id) and (UserLabels.userId eq userId)
		}
		return count == 1
	}
}