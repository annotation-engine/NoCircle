package com.nocircle.app.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [ConfigEntity::class], version = 1)
@ConstructedBy(NoCircleDatabaseConstructor::class)
abstract class NoCircleDatabase : RoomDatabase() {
	
	companion object {
		
		private val database by lazy { getRoomDatabase() }
		
		val configDao by lazy { database.getConfigDao() }
	}
	
	abstract fun getConfigDao(): ConfigDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object NoCircleDatabaseConstructor : RoomDatabaseConstructor<NoCircleDatabase> {
	
	override fun initialize(): NoCircleDatabase
}

private fun getRoomDatabase(): NoCircleDatabase {
	return getDatabaseBuilder()
		.addMigrations()
		.fallbackToDestructiveMigrationOnDowngrade(true)
		.setDriver(BundledSQLiteDriver())
		.setQueryCoroutineContext(Dispatchers.IO)
		.build()
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<NoCircleDatabase>