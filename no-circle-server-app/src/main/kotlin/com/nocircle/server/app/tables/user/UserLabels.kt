package com.nocircle.server.app.tables.user

import com.nocircle.server.common.tables.BaseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object UserLabels : BaseTable("tb_user_labels") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val label = varchar("label", 10)
	
	val color = varchar("color", 9)
}

class UserLabel(id: EntityID<Int>) : IntEntity(id) {
	
	companion object : IntEntityClass<UserLabel>(UserLabels)
	
	var label by UserLabels.label
	
	var color by UserLabels.color
}