package com.nocircle.server.app.dao

import com.nocircle.server.app.tables.UserLabel
import com.nocircle.server.app.tables.UserLabels
import com.nocircle.server.common.exposed.*
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll

object UserLabelDao {
	
	suspend fun getOneById(id: Int): UserLabel? {
		return UserLabels.selectAll()
			.where { UserLabels.id eq id }
			.logicExists(UserLabels)
			.singleOrNull()
			?.wrapRow(UserLabel)
	}
	
	suspend fun getListByUserId(userId: Int): List<UserLabel> {
		return UserLabels.selectAll()
			.where { UserLabels.userId eq userId }
			.logicExists(UserLabels)
			.orderBy(UserLabels.id)
			.wrapRows(UserLabel)
	}
	
	suspend fun getListByUserIdAndNeqId(userId: Int, id: Int): List<UserLabel> {
		return UserLabels.selectAll()
			.where { (UserLabels.userId eq userId) and (UserLabels.id neq id) }
			.logicExists(UserLabels)
			.orderBy(UserLabels.id)
			.wrapRows(UserLabel)
	}
	
	suspend fun getMapByUserIds(userIds: List<Int>): Map<Int, List<UserLabel>> {
		return UserLabels.selectAll()
			.where { UserLabels.userId inList userIds }
			.logicExists(UserLabels)
			.wrapRows(UserLabel)
			.groupBy { it.userId }
	}
	
	suspend fun insertOne(userId: Int, label: String, color: String): Boolean {
		val insert = UserLabels.insert {
			it[this.userId] = userId
			it[this.label] = label
			it[this.color] = color
		}
		return insert.insertedCount == 1
	}
	
	suspend fun updateOne(userId: Int, id: Int, label: String, color: String): Boolean {
		val count = UserLabels.logicUpdate(
			where = { (UserLabels.id eq id) and (UserLabels.userId eq userId) }
		) {
			it[this.label] = label
			it[this.color] = color
		}
		return count == 1
	}
	
	suspend fun deleteOne(userId: Int, id: Int): Boolean {
		val count = UserLabels.logicDeleteWhere {
			(UserLabels.id eq id) and (UserLabels.userId eq userId)
		}
		return count == 1
	}
}