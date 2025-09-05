package com.nocircle.server.common.exposed

import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.r2dbc.SizedIterable
import kotlin.reflect.KProperty

abstract class NoEntity(
	val id: EntityID<Int>
) {
	
	internal var resultRow: ResultRow? = null
	
	operator fun <T> Column<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
		return resultRow!![this]
	}
}

abstract class NoEntityClass<E : NoEntity>(
	private val table: NoTable,
	private val newEntity: (EntityID<Int>) -> E
) {
	
	fun wrapRow(resultRow: ResultRow): E {
		val id = resultRow.getOrNull(table.id) ?: NullEntityId(table)
		return newEntity(id).apply {
			this.resultRow = resultRow
		}
	}
	
	suspend fun wrapRows(resultRows: SizedIterable<ResultRow>): List<E> {
		return resultRows.toList().map { wrapRow(it) }
	}
}

class NullEntityId internal constructor(table: NoTable) : EntityID<Int>(table, null)

fun <E : NoEntity, EC : NoEntityClass<E>> ResultRow.wrapRow(
	entityClass: EC
): E = entityClass.wrapRow(this)

suspend fun <E : NoEntity, EC : NoEntityClass<E>> SizedIterable<ResultRow>.wrapRows(
	entityClass: EC
): List<E> = entityClass.wrapRows(this)