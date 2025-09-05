package com.nocircle.server.common.exposed

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.UpdateStatement
import org.jetbrains.exposed.v1.r2dbc.*
import org.jetbrains.exposed.v1.r2dbc.statements.ReturningSuspendExecutable
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

suspend inline fun <T> tx(
	db: R2dbcDatabase? = null,
	noinline statement: suspend R2dbcTransaction.() -> T
): T = suspendTransaction(db, statement)

suspend fun Query.exists(): Boolean = !this.empty()

fun <T : NoTable> T.selectWithout(
	column: Expression<*>,
	vararg columns: Expression<*>
): Query {
	val withoutColumns = columns.toList() + column
	val columns = this.columns.filterNot { it in withoutColumns }
	return this.select(columns)
}

fun Query.logicExists(
	table: NoTable,
	vararg tables: NoTable
): Query = this.andWhere {
	tables.fold(table.deleteFlag eq false) { acc, table ->
		acc.and { table.deleteFlag eq false }
	}
}

fun isLogicExists(
	table: NoTable,
	vararg tables: NoTable
): Op<Boolean> = tables.fold(table.deleteFlag eq false) { acc, table ->
	acc.and { table.deleteFlag eq false }
}

@OptIn(ExperimentalTime::class)
suspend fun <T : NoTable> T.logicUpdate(
	where: () -> Op<Boolean>,
	limit: Int? = null,
	body: T.(UpdateStatement) -> Unit
): Int = this.update(
	where = { deleteFlag eq false and where() },
	limit = limit
) {
	body(it)
	it[this.updateTime] = Clock.System.now()
}

@OptIn(ExperimentalTime::class)
fun <T : NoTable> T.logicUpdateReturning(
	returning: List<Expression<*>> = columns,
	where: () -> Op<Boolean>,
	body: T.(UpdateStatement) -> Unit
): ReturningSuspendExecutable = this.updateReturning(
	returning = returning,
	where = { deleteFlag eq false and where() }
) {
	body(it)
	it[this.updateTime] = Clock.System.now()
}

@OptIn(ExperimentalTime::class)
suspend fun <T : NoTable> T.logicDeleteWhere(
	limit: Int? = null,
	where: () -> Op<Boolean>
): Int = this.update(
	where = { deleteFlag eq false and where() },
	limit = limit
) {
	it[this.updateTime] = Clock.System.now()
	it[this.deleteFlag] = true
}

@OptIn(ExperimentalTime::class)
fun <T : NoTable> T.logicDeleteReturning(
	returning: List<Expression<*>> = columns,
	where: () -> Op<Boolean>
): ReturningSuspendExecutable = this.updateReturning(
	returning = returning,
	where = { deleteFlag eq false and where() }
) {
	it[this.updateTime] = Clock.System.now()
	it[this.deleteFlag] = true
}

fun Query.paginate(number: Int, size: Int): Query {
	require(number > 0) { "Number must be greater than 0." }
	require(size > 0) { "Number must be greater than 0." }
	return this.offset(((number - 1) * size).toLong()).limit(size)
}