package com.nocircle.app.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<NoCircleDatabase> {
	val dbFile = File(System.getProperty("java.io.tmpdir"), "no_circle.db")
	return Room.databaseBuilder(name = dbFile.absolutePath)
}