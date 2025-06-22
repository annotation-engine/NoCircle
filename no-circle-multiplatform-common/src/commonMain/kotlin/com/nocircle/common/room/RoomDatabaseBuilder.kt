package com.nocircle.common.room

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect inline fun <reified T : RoomDatabase> getDatabaseBuilder(dbName: String): RoomDatabase.Builder<T>

inline fun <reified T : RoomDatabase> getRoomDatabase(
	dbName: String,
	vararg migrations: Migration
): T = getDatabaseBuilder<T>(dbName)
	.addMigrations(*migrations)
	.fallbackToDestructiveMigrationOnDowngrade(true)
	.fallbackToDestructiveMigration(true)
	.setDriver(BundledSQLiteDriver())
	.setQueryCoroutineContext(Dispatchers.IO)
	.build()