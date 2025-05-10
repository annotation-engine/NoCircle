package com.nocircle.common.room.entity

import androidx.paging.PagingSource
import androidx.room.*

@Entity
internal data class LogEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val name: String,
	val level: String,
	val content: String,
	val timestamp: Long
)

@Dao
internal interface LogDao {
	
	@Insert
	suspend fun insert(entity: LogEntity)
	
	@Query("SELECT * FROM LogEntity")
	fun query(): PagingSource<Int, LogEntity>
	
	@Query("DELETE FROM LogEntity")
	suspend fun deleteAll(): Int
}