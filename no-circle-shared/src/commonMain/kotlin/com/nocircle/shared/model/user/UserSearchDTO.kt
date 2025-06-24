package com.nocircle.shared.model.user

import com.nocircle.shared.model.label.UserLabelDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserSearchDTO(
	val userId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val labels: List<UserLabelDTO>,
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