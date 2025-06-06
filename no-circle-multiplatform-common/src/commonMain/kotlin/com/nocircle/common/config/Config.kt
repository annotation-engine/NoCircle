@file:Suppress("UNCHECKED_CAST")

package com.nocircle.common.config

import com.nocircle.common.room.CommonDatabase
import com.nocircle.common.room.entity.ConfigEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

abstract class ConfigKey<T : Any>(
	private val key: String = ""
) {
	
	companion object {
		
		suspend fun clearAll(): Int {
			return CommonDatabase.INSTANCE.configDao().deleteAll()
		}
	}
	
	internal val configKey: String by lazy {
		key.ifBlank { this::class.qualifiedName!! }
	}
}

suspend inline fun <reified T : Any> ConfigKey<T>.set(value: T?) = this.set(value, serializer())

suspend inline fun <reified T : Any> ConfigKey<T>.get(): T? = this.get(serializer())

suspend fun <T : Any> ConfigKey<T>.clear(): Int {
	return CommonDatabase.INSTANCE.configDao().delete(this.configKey)
}

suspend fun <T : Any> ConfigKey<T>.set(value: T?, serializer: KSerializer<T>) {
	val value = value?.let { Json.encodeToString(serializer, it) }
	CommonDatabase.INSTANCE.configDao().insert(ConfigEntity(this.configKey, value))
}

suspend fun <T : Any> ConfigKey<T>.get(serializer: KSerializer<T>): T? {
	val entity = CommonDatabase.INSTANCE.configDao().query(this.configKey) ?: return null
	return entity.value?.let {
		try {
			Json.decodeFromString(serializer, it)
		} catch (_: Exception) {
			null
		}
	}
}

object TokenConfigKey : ConfigKey<String>("token")