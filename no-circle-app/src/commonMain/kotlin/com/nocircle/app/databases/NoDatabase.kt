package com.nocircle.app.databases

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.nocircle.common.room.getRoomDatabase

@Database(entities = [ConfigEntity::class], version = 1)
@ConstructedBy(NoDatabaseConstructor::class)
abstract class NoDatabase : RoomDatabase() {
	
	companion object {
		
		val INSTANCE by lazy { getRoomDatabase<NoDatabase>("no_circle") }
	}
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object NoDatabaseConstructor : RoomDatabaseConstructor<NoDatabase> {
	
	override fun initialize(): NoDatabase
}