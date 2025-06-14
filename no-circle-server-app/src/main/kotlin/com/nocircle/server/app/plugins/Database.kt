package com.nocircle.server.app.plugins

import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.app.tables.FriendRelationships
import com.nocircle.server.app.tables.UserLabels
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.app.tables.Users
import com.nocircle.server.common.log.NoLog
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.system.measureTimeMillis

fun configureDatabase() {
	val millis = measureTimeMillis {
		val database = Database.connect(
			url = yaml.mysql.url,
			user = yaml.mysql.user,
			driver = yaml.mysql.driver,
			password = yaml.mysql.password
		)
		transaction(database) {
			SchemaUtils.create(*tables)
		}
	}
	NoLog.info("Mysql connected used for ${millis / 1_000f} seconds.")
}

private val tables = arrayOf(
	Users,
	UserLabels,
	UserLogins,
	FriendRequests,
	FriendRelationships
)