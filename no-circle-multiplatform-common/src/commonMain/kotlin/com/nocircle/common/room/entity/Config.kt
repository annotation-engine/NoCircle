package com.nocircle.common.room.entity

import androidx.room.*

@Entity
internal data class ConfigEntity(
	@PrimaryKey
	val key: String,
	val value: String?
)

@Dao
internal interface ConfigDao {
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(entity: ConfigEntity)
	
	@Query("SELECT * FROM ConfigEntity WHERE `key` = :key")
	suspend fun query(key: String): ConfigEntity?
	
	@Query("DELETE FROM ConfigEntity WHERE `key` = :key")
	suspend fun delete(key: String): Int
	
	@Query("DELETE FROM ConfigEntity")
	suspend fun deleteAll(): Int
}