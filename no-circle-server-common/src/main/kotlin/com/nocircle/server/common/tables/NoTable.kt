package com.nocircle.server.common.tables

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.timestamp

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