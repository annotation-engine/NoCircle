package com.nocircle.server.common.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.statements.UpdateStatement
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update

fun Query.exists(): Boolean = !this.empty()

fun <T : NoTable> T.selectWithout(
	column: Expression<*>,
	vararg columns: Expression<*>
): Query {
	val withoutColumns = columns.toList() + column
	val columns = this.columns.filter { it !in withoutColumns }
	return this.select(columns)
}

fun Query.logicExists(
	table: NoTable,
	vararg tables: NoTable
): Query {
	return this.andWhere {
		tables.fold(table.deleteFlag eq false) { acc, table ->
			acc.and { table.deleteFlag eq false }
		}
	}
}

fun <T : NoTable> T.logicUpdate(
	where: SqlExpressionBuilder.() -> Op<Boolean>,
	limit: Int? = null,
	body: T.(UpdateStatement) -> Unit
): Int {
	return this.update(
		where = { deleteFlag eq false and where() },
		limit = limit
	) {
		body(it)
		it[this.updateTime] = Clock.System.now()
	}
}

fun <T : NoTable> T.logicDeleteWhere(
	limit: Int? = null,
	op: T.(ISqlExpressionBuilder) -> Op<Boolean>
): Int {
	return this.logicUpdate(
		where = { op(this) },
		limit = limit,
	) {
		it[this.deleteFlag] = true
	}
}