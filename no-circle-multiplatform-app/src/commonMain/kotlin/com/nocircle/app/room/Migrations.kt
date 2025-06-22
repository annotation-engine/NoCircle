package com.nocircle.app.room

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
	override fun migrate(connection: SQLiteConnection) {
		val sql = """
			ALTER TABLE tb_friend_list DROP COLUMN createTime
		""".trimIndent()
		connection.execSQL(sql)
	}
}