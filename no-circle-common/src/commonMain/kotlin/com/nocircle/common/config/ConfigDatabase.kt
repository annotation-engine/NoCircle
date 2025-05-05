package com.nocircle.common.config

import androidx.room.*
import com.nocircle.common.room.getRoomDatabase

@Database(entities = [ConfigEntity::class], version = 1)
@ConstructedBy(ConfigDatabaseConstructor::class)
internal abstract class ConfigDatabase : RoomDatabase() {
	
	companion object {
		
		val INSTANCE by lazy { getRoomDatabase<ConfigDatabase>("no_circle_config") }
	}
	
	abstract val configDao: ConfigDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
internal expect object ConfigDatabaseConstructor : RoomDatabaseConstructor<ConfigDatabase> {
	
	override fun initialize(): ConfigDatabase
}

@Entity
internal data class ConfigEntity(
	@PrimaryKey
	val key: String,
	val value: String?
)

@Dao
internal interface ConfigDao {
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(obj: ConfigEntity)
	
	@Query("SELECT * FROM ConfigEntity WHERE `key` = :key")
	suspend fun query(key: String): ConfigEntity?
	
	@Query("DELETE FROM ConfigEntity WHERE `key` = :key")
	suspend fun clear(key: String)
	
	@Query("DELETE FROM ConfigEntity")
	suspend fun clearAll()
}