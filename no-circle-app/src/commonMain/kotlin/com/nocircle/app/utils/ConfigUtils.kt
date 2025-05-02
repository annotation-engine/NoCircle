package com.nocircle.app.utils

import com.nocircle.app.databases.ConfigEntity
import com.nocircle.app.databases.NoDaos
import kotlinx.serialization.json.Json

object ConfigUtils {
	
	suspend inline fun <reified T : Any> setValue(key: String, value: T?) {
		val json = if (value != null) Json.encodeToString(value) else null
		NoDaos.config.insert(ConfigEntity(key, json))
	}
	
	suspend inline fun <reified T : Any> getValue(key: String, default: T? = null): T? {
		val entity = NoDaos.config.query(key) ?: return default
		if (entity.value == null) return null
		return Json.decodeFromString<T>(entity.value)
	}
	
	suspend fun clear(key: String) {
		NoDaos.config.delete(key)
	}
	
	suspend fun clearAll() {
		NoDaos.config.deleteAll()
	}
}