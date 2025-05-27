package com.nocircle.server.app.tables.user

import com.nocircle.server.common.exposed.BaseTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserLabels : BaseTable("tb_user_labels") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 16)
	
	val color = varchar("color", 9)
}

class UserLabel(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<UserLabel>(UserLabels)
	
	var label by UserLabels.label
	
	var color by UserLabels.color
}