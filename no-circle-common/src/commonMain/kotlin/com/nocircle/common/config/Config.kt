package com.nocircle.common.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

suspend inline fun <reified T> setConfig(key: String, value: T) {
	setConfig(key, value, serializer())
}

suspend fun <T> setConfig(key: String, value: T, serializer: KSerializer<T>) {
	val value = value?.let { Json.encodeToString(serializer, it) }
	ConfigDatabase.INSTANCE.configDao.insert(ConfigEntity(key, value))
}

suspend inline fun <reified T : Any> getConfig(key: String): T {
	return getConfig(key, serializer())
}

suspend fun <T : Any> getConfig(key: String, serializer: KSerializer<T>): T {
	val entity = ConfigDatabase.INSTANCE.configDao.query(key)!!
	return entity.value!!.let { Json.decodeFromString(serializer, it) }
}

suspend inline fun <reified T : Any> getConfigOrNull(key: String, default: T? = null): T? {
	return getConfigOrNull(key, default, serializer())
}

suspend fun <T : Any> getConfigOrNull(key: String, default: T?, serializer: KSerializer<T>): T? {
	val entity = ConfigDatabase.INSTANCE.configDao.query(key) ?: return default
	return entity.value?.let { Json.decodeFromString(serializer, it) }
}

suspend fun deleteConfig(key: String): Int {
	return ConfigDatabase.INSTANCE.configDao.delete(key)
}

suspend fun deleteAllConfig(): Int {
	return ConfigDatabase.INSTANCE.configDao.deleteAll()
}