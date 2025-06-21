package com.nocircle.common.room.entity

import androidx.room.*

@Entity("tb_config")
internal class ConfigEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val userId: Int,
	val key: String,
	val value: String?
)

@Dao
internal interface ConfigDao {
	
	@Insert
	suspend fun insert(entity: ConfigEntity)
	
	@Query("SELECT * FROM tb_config WHERE userId = :userId AND `key` = :key")
	suspend fun query(userId: Int, key: String): ConfigEntity?
	
	@Query("UPDATE tb_config SET value = :value WHERE `key` = :key AND userId = :userId")
	suspend fun update(userId: Int, key: String, value: String?): Int
	
	@Query("DELETE FROM tb_config WHERE userId = :userId AND `key` = :key")
	suspend fun delete(userId: Int, key: String): Int
	
	@Query("DELETE FROM tb_config WHERE userId = :userId")
	suspend fun deleteAll(userId: Int): Int
	
	@Query("SELECT COUNT(*) > 0 FROM tb_config WHERE userId = :userId AND `key` = :key")
	suspend fun exists(userId: Int?, key: String): Boolean
}