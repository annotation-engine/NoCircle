package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserLabels : NoTable("tb_user_label") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 20)
	
	val color = char("color", 9)
}

class UserLabel(id: EntityID<Int>) : NoIntEntity(id, UserLabels) {
	
	companion object : IntEntityClass<UserLabel>(UserLabels)
	
	var label by UserLabels.label
	
	var color by UserLabels.color
}