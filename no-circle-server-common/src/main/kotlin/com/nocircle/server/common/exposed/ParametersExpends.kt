package com.nocircle.server.common.exposed

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlin.enums.enumEntries

context(_: RoutingContext)
fun Parameters.getString(key: String): String = this.getOrFail(key)

context(_: RoutingContext)
fun Parameters.getStringOrNull(key: String): String? = this[key]

context(_: RoutingContext)
fun Parameters.getInt(key: String): Int = this.getOrFail(key).toInt()

context(_: RoutingContext)
fun Parameters.getIntOrNull(key: String): Int? = this[key]?.toIntOrNull()

context(_: RoutingContext)
fun Parameters.getBoolean(key: String): Boolean = this.getOrFail(key).toBooleanStrict()

context(_: RoutingContext)
fun Parameters.getBooleanOrNull(key: String): Boolean? = this[key]?.toBooleanStrictOrNull()

context(_: RoutingContext)
fun Parameters.getDouble(key: String): Double = this.getOrFail(key).toDouble()

context(_: RoutingContext)
fun Parameters.getDoubleOrNull(key: String): Double? = this[key]?.toDouble()

context(_: RoutingContext)
inline fun <reified E : Enum<E>> Parameters.getEnum(key: String): E {
	val value = this.getOrFail(key)
	return enumEntries<E>().first { it.name.equals(value, ignoreCase = true) }
}

context(_: RoutingContext)
inline fun <reified E : Enum<E>> Parameters.getEnumOrNull(key: String): E? {
	val value = this[key]
	return enumEntries<E>().find { it.name.equals(value, ignoreCase = true) }
}