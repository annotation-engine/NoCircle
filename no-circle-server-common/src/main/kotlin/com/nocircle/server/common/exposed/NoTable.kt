package com.nocircle.server.common.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.timestamp

open class BaseTable(
	name: String,
	columnName: String = "id"
) : IntIdTable(name, columnName) {
	
	val createTime = timestamp("create_time")
		.clientDefault { Clock.System.now() }
	
	val updateTime = timestamp("update_time")
		.clientDefault { Clock.System.now() }
	
	val deleteFlag = bool("delete_flag")
		.default(false)
}