package com.nocircle.app.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.nocircle.app.room.converters.LocalDateTimeConverter
import com.nocircle.app.room.entity.FriendListDao
import com.nocircle.app.room.entity.FriendListEntity
import com.nocircle.common.room.DB_VERSION
import com.nocircle.common.room.getRoomDatabase

@Database(
	entities = [FriendListEntity::class],
	version = DB_VERSION
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
	
	companion object {
		
		private const val DB_NAME = "no_circle_app"
		
		val INSTANCE by lazy { getRoomDatabase<AppDatabase>(DB_NAME) }
	}
	
	abstract fun getFriendListDao(): FriendListDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
	
	override fun initialize(): AppDatabase
}