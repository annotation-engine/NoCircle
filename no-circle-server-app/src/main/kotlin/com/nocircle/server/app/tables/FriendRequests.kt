package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoEntity
import com.nocircle.server.common.exposed.NoEntityClass
import com.nocircle.server.common.exposed.NoTable
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import kotlin.time.ExperimentalTime

object FriendRequests : NoTable("friend_request") {
	
	val senderId = integer("sender_id")
		.references(Users.id)
	
	val receiverId = integer("receiver_id")
		.references(Users.id)
	
	val status = enumerationByName<FriendRequestDTO.Status>("status", 8)
}

class FriendRequest(id: EntityID<Int>) : NoEntity(id) {
	
	companion object Companion : NoEntityClass<FriendRequest>(FriendRequests, ::FriendRequest)
	
	val senderId by FriendRequests.senderId
	
	val receiverId by FriendRequests.receiverId
	
	val status by FriendRequests.status
	
	@OptIn(ExperimentalTime::class)
	val createTime by FriendRequests.createTime
}