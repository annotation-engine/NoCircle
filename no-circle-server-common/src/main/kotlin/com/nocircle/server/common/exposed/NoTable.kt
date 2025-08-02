package com.nocircle.server.common.exposed

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

abstract class NoTable(
	name: String,
	columnName: String = "id"
) : IntIdTable(name, columnName) {
	
	@OptIn(ExperimentalTime::class)
	val createTime = timestamp("create_time")
		.clientDefault { Clock.System.now() }
	
	@OptIn(ExperimentalTime::class)
	val updateTime = timestamp("update_time")
		.clientDefault { Clock.System.now() }
	
	val deleteFlag = bool("delete_flag")
		.default(false)
}