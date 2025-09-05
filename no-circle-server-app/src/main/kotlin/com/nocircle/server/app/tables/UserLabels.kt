package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoEntity
import com.nocircle.server.common.exposed.NoEntityClass
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object UserLabels : NoTable("user_label") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 20)
	
	val color = char("color", 9)
}

class UserLabel(id: EntityID<Int>) : NoEntity(id) {
	
	companion object : NoEntityClass<UserLabel>(UserLabels, ::UserLabel)
	
	val userId by UserLabels.userId
	
	val label by UserLabels.label
	
	val color by UserLabels.color
}