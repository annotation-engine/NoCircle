package com.nocircle.app.room.entity

import androidx.room.*
import kotlinx.datetime.LocalDateTime

@Entity("tb_friend_list")
class FriendListEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val userId: Int,
	val friendId: Int,
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val pinyin: String,
	val createTime: LocalDateTime
)

@Dao
interface FriendListDao {
	
	@Insert
	suspend fun insert(entity: FriendListEntity)
	
	@Query("SELECT * FROM tb_friend_list WHERE userId = :userId")
	suspend fun queryList(userId: Int): List<FriendListEntity>
	
	@Query("DELETE FROM tb_friend_list WHERE userId = :userId")
	suspend fun deleteAll(userId: Int): Int
}