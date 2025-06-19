package com.nocircle.server.app.tables

import com.nocircle.server.common.exposed.NoIntEntity
import com.nocircle.server.common.exposed.NoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

object FriendRequests : NoTable("tb_friend_request") {
	
	val senderId = integer("sender_id")
	
	val receiverId = integer("receiver_id")
	
	val status = enumerationByName<Status>("status", 8)
	
	enum class Status { AGREED, REJECTED, WAITING, CANCELED }
}

class FriendRequest(id: EntityID<Int>) : NoIntEntity<FriendRequests>(id, FriendRequests) {
	
	companion object Companion : IntEntityClass<FriendRequest>(FriendRequests)
	
	val senderId by table.senderId
	
	val receiverId by table.receiverId
	
	val status by table.status
}