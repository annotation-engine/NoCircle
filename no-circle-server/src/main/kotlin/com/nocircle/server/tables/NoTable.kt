package com.nocircle.server.tables

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class BaseTable(name: String, columnName: String = "id") : IntIdTable(name, columnName) {
	
	val createTime = timestamp("create_time")
		.clientDefault { Clock.System.now() }
	
	val updateTime = timestamp("update_time")
		.clientDefault { Clock.System.now() }
	
	val deleteFlag = bool("delete_flag")
		.default(false)
}

typealias IntEntityID = EntityID<Int>

context(_: SqlExpressionBuilder)
val BaseTable.isLogicExists: Op<Boolean>
	get() = this.deleteFlag eq false

context(_: SqlExpressionBuilder)
fun isLogicExists(table: BaseTable, vararg tables: BaseTable): Op<Boolean> {
	return tables.fold(table.isLogicExists) { acc, it ->
		acc.and { it.isLogicExists }
	}
}