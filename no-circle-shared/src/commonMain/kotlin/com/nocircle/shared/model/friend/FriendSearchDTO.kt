package com.nocircle.shared.model.friend

import com.nocircle.shared.model.label.LabelDTO
import kotlinx.serialization.Serializable

@Serializable
data class FriendSearchDTO(
	val userId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val labels: List<LabelDTO>,
	val relationship: RelationshipDTO,
	val isAlreadySend: Boolean
) {
	
	@Serializable
	enum class RelationshipDTO {
		FRIEND,
		OWNER,
		STRANGER
	}
}