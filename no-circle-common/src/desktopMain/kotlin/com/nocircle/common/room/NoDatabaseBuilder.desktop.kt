package com.nocircle.common.room

import androidx.room.Room
import androidx.room.RoomDatabase
import com.nocircle.common.device.DeviceName
import com.nocircle.common.device.NoDevice
import com.nocircle.common.log.NoLog
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

actual inline fun <reified T : RoomDatabase> getDatabaseBuilder(dbName: String): RoomDatabase.Builder<T> {
	val dbName = if (dbName.endsWith(".db")) dbName else "$dbName.db"
	val appName = "NoCircle"
	val paths = when (NoDevice.Name) {
		DeviceName.MacOS -> Paths.get(System.getProperty("user.home"), "Library", "Application Support", appName, "data")
		DeviceName.Linux -> Paths.get(System.getProperty("user.home"), ".local", "share", appName)
		DeviceName.Windows -> Paths.get(System.getenv("LOCALAPPDATA"), appName, "data")
		else -> error("Unknown device.")
	}
	val absolutePath = paths.resolve(dbName).absolutePathString()
	NoLog.info(absolutePath)
	return Room.databaseBuilder(absolutePath)
}