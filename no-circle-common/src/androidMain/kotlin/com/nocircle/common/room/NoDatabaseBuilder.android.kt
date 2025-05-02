package com.nocircle.common.room

import androidx.room.Room
import androidx.room.RoomDatabase
import com.nocircle.common.utils.Globals

actual inline fun <reified T : RoomDatabase> getDatabaseBuilder(dbName: String): RoomDatabase.Builder<T> {
	val dbName = if (dbName.endsWith(".db")) dbName else "$dbName.db"
	val context = requireNotNull(Globals.applicationContext?.get())
	val dbFile = context.getDatabasePath(dbName)
	return Room.databaseBuilder(context, dbFile.name)
}