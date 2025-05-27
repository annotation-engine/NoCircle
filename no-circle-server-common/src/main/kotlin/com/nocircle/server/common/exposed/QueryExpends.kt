package com.nocircle.server.common.exposed

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.andWhere

fun Query.exists(): Boolean = !this.empty()

fun Query.isLogicExists(
	table: BaseTable,
	vararg tables: BaseTable
): Query = this.andWhere {
	tables.fold(table.deleteFlag eq false) { acc, table ->
		acc.and { table.deleteFlag eq false }
	}
}