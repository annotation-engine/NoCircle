package com.nocircle.app.databases

import androidx.room.*

@Entity
data class ConfigEntity(
	@PrimaryKey
	val key: String,
	val value: String?
)

@Dao
interface ConfigDao {
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(entity: ConfigEntity)
	
	@Query("SELECT * FROM ConfigEntity WHERE `key` = :key")
	suspend fun query(key: String): ConfigEntity?
	
	@Query("DELETE FROM ConfigEntity WHERE `key` = :key")
	suspend fun delete(key: String)
	
	@Query("DELETE FROM ConfigEntity")
	suspend fun deleteAll()
}