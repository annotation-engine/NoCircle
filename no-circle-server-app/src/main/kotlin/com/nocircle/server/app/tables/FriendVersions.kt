package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoTable

object FriendVersions : NoTable("friend_version") {
	
	val userId = integer("user_id")
		.references(Users.id)
	
	val version = integer("version")
}