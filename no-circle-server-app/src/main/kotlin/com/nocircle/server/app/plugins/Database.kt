package com.nocircle.server.app.plugins

import com.nocircle.server.app.tables.*
import com.nocircle.server.common.log.NoLog
import io.r2dbc.spi.ConnectionFactoryOptions
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlin.system.measureTimeMillis

suspend fun configureDatabase() {
	val millis = measureTimeMillis {
		val database = R2dbcDatabase.connect {
			connectionFactoryOptions {
				option(ConnectionFactoryOptions.DRIVER, yaml.database.driver)
				option(ConnectionFactoryOptions.HOST, yaml.database.host)
				option(ConnectionFactoryOptions.PORT, yaml.database.port)
				option(ConnectionFactoryOptions.DATABASE, yaml.database.name)
				option(ConnectionFactoryOptions.USER, yaml.database.user)
				option(ConnectionFactoryOptions.PASSWORD, yaml.database.password)
			}
		}
		suspendTransaction(database) {
			org.jetbrains.exposed.v1.r2dbc.SchemaUtils.create(
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