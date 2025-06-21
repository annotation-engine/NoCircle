package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserLabels : NoTable("tb_user_label") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 20)
	
	val color = char("color", 9)
}

class UserLabel(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<UserLabel>(UserLabels)
	
	val userId by UserLabels.userId
	
	val label by UserLabels.label
	
	val color by UserLabels.color
}