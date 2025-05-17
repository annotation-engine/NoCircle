package com.nocircle.common.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.nocircle.common.room.entity.ConfigDao
import com.nocircle.common.room.entity.ConfigEntity
import com.nocircle.common.room.entity.LogDao
import com.nocircle.common.room.entity.LogEntity

@Database(entities = [ConfigEntity::class, LogEntity::class], version = 1)
@ConstructedBy(CommonDatabaseConstructor::class)
internal abstract class CommonDatabase : RoomDatabase() {
	
	companion object {
		
		private const val DB_NAME = "no_circle_common"
		
		val INSTANCE by lazy { getRoomDatabase<CommonDatabase>(DB_NAME) }
	}
	
	abstract fun configDao(): ConfigDao
	
	abstract fun configLog(): LogDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
internal expect object CommonDatabaseConstructor : RoomDatabaseConstructor<CommonDatabase> {
	
	override fun initialize(): CommonDatabase
}