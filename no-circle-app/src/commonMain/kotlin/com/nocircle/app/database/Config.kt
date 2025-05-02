package com.nocircle.app.database

import androidx.room.*
import kotlinx.serialization.json.Json

@Entity(tableName = "tb_config")
data class ConfigEntity(
	@PrimaryKey
	val key: String,
	val value: String
)

@Dao
interface ConfigDao {
	
	companion object {
		
		suspend inline fun <reified T> setValue(key: String, value: T) {
			val json = Json.encodeToString(value)
			NoCircleDatabase.configDao.insert(ConfigEntity(key, json))
		}
		
		suspend inline fun <reified T> getValue(key: String, default: T? = null): T? {
			val json = NoCircleDatabase.configDao.queryOne(key)?.value ?: return default
			return Json.decodeFromString(json)
		}
	}
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(entity: ConfigEntity)
	
	@Query("SELECT * FROM tb_config WHERE `key` = :key")
	suspend fun queryOne(key: String): ConfigEntity?
}