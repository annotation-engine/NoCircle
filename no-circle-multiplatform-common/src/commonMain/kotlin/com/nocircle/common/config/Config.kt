@file:Suppress("UNCHECKED_CAST")

package com.nocircle.common.config

import com.nocircle.common.room.CommonDatabase
import com.nocircle.common.room.entity.ConfigEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

abstract class ConfigKey<T : Any>(
	val key: String,
	private val isOwn: Boolean = false
) {
	val cacheMap = mutableMapOf<Int?, T?>()
	
	suspend fun getUserId(): Int? = if (isOwn) UserIdConfigKey.get() else null
}

suspend inline fun <reified T : Any> ConfigKey<T>.set(value: T?) = this.set(value, serializer())

suspend inline fun <reified T : Any> ConfigKey<T>.get(): T? = this.get(serializer())

suspend fun <T : Any> ConfigKey<T>.clear() {
	val userId = getUserId()
	cacheMap -= userId
	val configDao = CommonDatabase.INSTANCE.configDao()
	configDao.delete(userId, key)
}

suspend fun <T : Any> ConfigKey<T>.set(value: T?, serializer: KSerializer<T>) {
	val json = value?.let { Json.encodeToString(serializer, it) }
	val userId = getUserId()
	cacheMap[userId] = value
	val configDao = CommonDatabase.INSTANCE.configDao()
	val exists = configDao.exists(userId, key)
	if (exists) {
		configDao.update(userId, key, json)
	} else {
		configDao.insert(ConfigEntity(userId = userId, key = key, value = json))
	}
}

suspend fun <T : Any> ConfigKey<T>.get(serializer: KSerializer<T>): T? {
	val userId = getUserId()
	return if (userId in cacheMap) {
		cacheMap[userId]
	} else {
		val configDao = CommonDatabase.INSTANCE.configDao()
		val entity = configDao.query(userId, key) ?: return null
		val value = entity.value?.let { Json.decodeFromString(serializer, it) }
		cacheMap[userId] = value
		value
	}
}

object TokenConfigKey : ConfigKey<String>("token")

object UserIdConfigKey : ConfigKey<Int>("userId")