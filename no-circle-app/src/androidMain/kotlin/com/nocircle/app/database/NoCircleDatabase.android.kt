package com.nocircle.app.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.nocircle.app.NoCircleApplication

actual fun getDatabaseBuilder(): RoomDatabase.Builder<NoCircleDatabase> {
	val context = NoCircleApplication.getContext()
	val dbFile = context.getDatabasePath("no_circle.db")
	return Room.databaseBuilder(context = context, name = dbFile.absolutePath)
}