package com.nocircle.server.common.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.datetime.timestamp

abstract class NoTable(
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

abstract class NoIntEntity(
	id: EntityID<Int>,
	table: NoTable
) : IntEntity(id) {
	
	val createTime by table.createTime
	
	val updateTime by table.updateTime
	
	val deleteFlag by table.deleteFlag
}