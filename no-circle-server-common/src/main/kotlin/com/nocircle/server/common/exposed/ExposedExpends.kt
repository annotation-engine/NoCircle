package com.nocircle.server.common.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.statements.UpdateStatement
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.statements.ReturningBlockingExecutable

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
	limit: Int? = null,
	body: T.(UpdateStatement) -> Unit
): Int {
	return this.update(
		where = { deleteFlag eq false },
		limit = limit
	) {
		body(it)
		it[this.updateTime] = Clock.System.now()
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

fun <T : NoTable> T.logicUpdateReturning(
	returning: List<Expression<*>> = columns,
	body: T.(UpdateStatement) -> Unit
): ReturningBlockingExecutable {
	return this.updateReturning(
		returning = returning,
		where = { deleteFlag eq false }
	) {
		body(it)
		it[this.updateTime] = Clock.System.now()
	}
}

fun <T : NoTable> T.logicUpdateReturning(
	returning: List<Expression<*>> = columns,
	where: SqlExpressionBuilder.() -> Op<Boolean>,
	body: T.(UpdateStatement) -> Unit
): ReturningBlockingExecutable {
	return this.updateReturning(
		returning = returning,
		where = { deleteFlag eq false and where() }
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

fun <T : NoTable> T.logicDeleteReturning(
	returning: List<Expression<*>> = columns,
	where: SqlExpressionBuilder.() -> Op<Boolean>
): ReturningBlockingExecutable {
	return this.logicUpdateReturning(
		returning = returning,
		where = where
	) {
		it[this.deleteFlag] = true
	}
}

fun <T : NoTable> T.logicDeleteAll(): Int {
	return this.logicDeleteWhere {
		this.deleteFlag eq false
	}
}