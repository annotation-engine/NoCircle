package com.nocircle.server.app.plugins

import com.nocircle.server.app.tables.*
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.log.NoLog
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import kotlin.system.measureTimeMillis

suspend fun configureDatabase() {
	val millis = measureTimeMillis {
		val database = Database.connect(
			url = yaml.database.url,
			user = yaml.database.user,
			password = yaml.database.password,
		)
		tx(database) {
			SchemaUtils.create(
				Users,
				UserDetails,
				UserLabels,
				UserLogins,
				FriendRequests,
				FriendRelationships
			)
		}
	}
	NoLog.info("Postgresql connected used for ${millis / 1_000f} seconds.")
}