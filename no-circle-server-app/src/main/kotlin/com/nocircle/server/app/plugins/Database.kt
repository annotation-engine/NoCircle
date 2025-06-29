package com.nocircle.server.app.plugins

import com.nocircle.server.app.tables.*
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
			password = yaml.mysql.password
		)
		transaction(database) {
			SchemaUtils.create(*tables)
		}
	}
	NoLog.info("Postgresql connected used for ${millis / 1_000f} seconds.")
}

private val tables = arrayOf(
	Users,
	UserLabels,
	UserLogins,
	FriendRequests,
	FriendRelationships,
	FriendVersions
)