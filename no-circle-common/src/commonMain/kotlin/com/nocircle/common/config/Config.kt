package com.nocircle.common.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object Config {
	
	suspend inline operator fun <reified T> set(key: String, value: T) {
		this.setValue(key, serializer<T>(), value)
	}
	
	suspend inline operator fun <reified T : Any> get(key: String): T? {
		return this.getValue(key, serializer<T>())
	}
	
	suspend fun <T> setValue(key: String, serializer: KSerializer<T>, value: T) {
		val json = Json.encodeToString(serializer, value)
		ConfigDatabase.INSTANCE.configDao.insert(ConfigEntity(key, json))
	}
	
	suspend fun <T : Any> getValue(key: String, serializer: KSerializer<T>, default: T? = null): T? {
		val entity = ConfigDatabase.INSTANCE.configDao.query(key) ?: return default
		return entity.value?.let { Json.decodeFromString(serializer, it) }
	}
	
	suspend inline fun <reified T : Any> getValue(key: String, default: T? = null): T? {
		return this.getValue(key, serializer<T>(), default)
	}
}