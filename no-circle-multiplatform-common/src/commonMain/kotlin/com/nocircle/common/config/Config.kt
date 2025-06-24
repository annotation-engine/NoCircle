@file:Suppress("UNCHECKED_CAST")

package com.nocircle.common.config

import com.nocircle.common.room.CommonDatabase
import com.nocircle.common.room.entity.ConfigEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

abstract class ConfigKey<T : Any>(
	val key: String,
	val single: Boolean = false
) {
	val cacheMap = mutableMapOf<Int, T?>()
}

private const val SINGLE_USER_ID = -1

suspend inline fun <reified T : Any> ConfigKey<T>.set(value: T?) = this.set(value, serializer())

suspend inline fun <reified T : Any> ConfigKey<T>.get(): T? = this.get(serializer())

suspend fun getUserId(single: Boolean) = if (single) SINGLE_USER_ID else UserIdConfigKey.get() ?: SINGLE_USER_ID

suspend fun <T : Any> ConfigKey<T>.clear() {
	val userId = getUserId(single)
	cacheMap -= userId
	val configDao = CommonDatabase.INSTANCE.getConfigDao()
	configDao.delete(userId, key)
}

suspend fun <T : Any> ConfigKey<T>.set(value: T?, serializer: KSerializer<T>) {
	val userId = getUserId(single)
	cacheMap[userId] = value
	val configDao = CommonDatabase.INSTANCE.getConfigDao()
	val exists = configDao.exists(userId, key)
	val json = value?.let { Json.encodeToString(serializer, it) }
	if (exists) {
		configDao.update(userId, key, json)
	} else {
		configDao.insert(ConfigEntity(userId = userId, key = key, value = json))
	}
}

suspend fun <T : Any> ConfigKey<T>.get(serializer: KSerializer<T>): T? {
	val userId = getUserId(single)
	if (userId in cacheMap) {
		return cacheMap[userId]!!
	}
	val configDao = CommonDatabase.INSTANCE.getConfigDao()
	val entity = configDao.query(userId, key) ?: return null
	val value = if (entity.value != null) {
		Json.decodeFromString(serializer, entity.value)
	} else null
	cacheMap[userId] = value
	return value
}

object TokenConfigKey : ConfigKey<String>("token", single = true)

object UserIdConfigKey : ConfigKey<Int>("userId", single = true)