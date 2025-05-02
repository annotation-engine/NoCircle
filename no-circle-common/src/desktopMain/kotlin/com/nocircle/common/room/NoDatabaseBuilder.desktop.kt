package com.nocircle.common.room

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual inline fun <reified T : RoomDatabase> getDatabaseBuilder(dbName: String): RoomDatabase.Builder<T> {
	val dbName = if (dbName.endsWith(".db")) dbName else "$dbName.db"
	val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)
	return Room.databaseBuilder(dbFile.absolutePath)
}