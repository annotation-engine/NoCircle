package com.nocircle.service.plugins

import com.nocircle.service.tables.UserTable
import com.nocircle.service.utils.Log
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.measureTimeMillis

fun configureMysql() {
	val millis = measureTimeMillis {
		val database = Database.connect(
			url = yaml.mysql.url,
			user = yaml.mysql.user,
			driver = yaml.mysql.driver,
			password = yaml.mysql.password
		)
		transaction(database) {
			SchemaUtils.create(
				UserTable
			)
		}
	}
	Log.info("Mysql connected used for ${millis / 1_000f} seconds.")
}