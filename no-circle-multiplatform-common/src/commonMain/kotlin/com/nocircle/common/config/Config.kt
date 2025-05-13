@file:Suppress("UNCHECKED_CAST")

package com.nocircle.common.config

import com.nocircle.common.room.CommonDatabase
import com.nocircle.common.room.entity.ConfigEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object Config {
	
	suspend inline operator fun <reified T : Any> set(key: ConfigKey<T>, value: T) = key.set(value)
	
	suspend inline operator fun <reified T : Any> get(key: ConfigKey<T>): T? = key.get()
	
	suspend fun clear(key: ConfigKey<*>): Int = key.clear()
	
	suspend fun clearAll(): Int {
		return CommonDatabase.INSTANCE.configDao().deleteAll()
	}
}

interface ConfigKey<T : Any>

suspend inline fun <reified T : Any> ConfigKey<T>.set(value: T?) = this.set(value, serializer())

suspend inline fun <reified T : Any> ConfigKey<T>.get(): T? = this.get(serializer())

suspend fun ConfigKey<*>.clear(): Int {
	return CommonDatabase.INSTANCE.configDao().delete(this.key)
}

suspend fun <T : Any> ConfigKey<T>.set(value: T?, serializer: KSerializer<T>) {
	val value = value?.let { Json.encodeToString(serializer, it) }
	CommonDatabase.INSTANCE.configDao().insert(ConfigEntity(this.key, value))
}

suspend fun <T : Any> ConfigKey<T>.get(serializer: KSerializer<T>): T? {
	val entity = CommonDatabase.INSTANCE.configDao().query(this.key) ?: return null
	return entity.value?.let { Json.decodeFromString(serializer, it) }
}

private val ConfigKey<*>.key: String get() = this::class.qualifiedName ?: error("ConfigKey is not available!")

object TokenConfigKey : ConfigKey<String>