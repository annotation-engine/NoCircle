package com.nocircle.app.pages.main.friends.list

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.room.AppDatabase
import com.nocircle.app.room.entity.FriendListEntity
import com.nocircle.common.config.*
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FriendsListViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	private val _friendList = MutableStateFlow<List<FriendDTO>>(emptyList())
	val friendList = _friendList.asStateFlow()
	
	val sortOrder = MutableStateFlow(SortOrder.ASC)
	val sortBy = MutableStateFlow(SortBy.Nickname)
	
	init {
		viewModelScope.launch {
			async { loadFriendList() }
			async {
				sortOrder.collect {
					_friendList.value = friendList.value.sorted()
				}
			}
			async {
				sortBy.collect {
					_friendList.value = friendList.value.sorted()
				}
			}
		}
	}
	
	suspend fun loadFriendList() {
		val userId = UserIdConfigKey.get()
		val friendVersionResult = ktorfitx.friendApi.queryFriendVersion() ?: return networkError()
		if (!friendVersionResult.success) {
			return showNoErrorSnackbar(friendVersionResult.msg)
		}
		val friendListDao = AppDatabase.INSTANCE.getFriendListDao()
		
		val version = FriendVersionConfigKey.getOrNull() ?: -1
		
		val friendList = if (friendVersionResult.data!! == version) {
			friendListDao.queryList(userId).map {
				FriendDTO(
					friendId = it.friendId,
					username = it.username,
					nickname = it.nickname,
					avatarUrl = it.avatarUrl,
					pinyin = it.pinyin,
					createTime = it.createTime
				)
			}
		} else {
			val result = ktorfitx.friendApi.queryFriendList() ?: return networkError()
			if (!result.success) {
				return showNoErrorSnackbar(result.msg)
			}
			FriendVersionConfigKey.set(friendVersionResult.data!!)
			friendListDao.deleteAll(userId)
			result.data!!.forEach {
				val entity = FriendListEntity(
					userId = userId,
					friendId = it.friendId,
					username = it.username,
					nickname = it.nickname,
					avatarUrl = it.avatarUrl,
					pinyin = it.pinyin,
					createTime = it.createTime
				)
				friendListDao.insert(entity)
			}
			result.data!!
		}
		_friendList.value = friendList.sorted()
	}
	
	private fun List<FriendDTO>.sorted(): List<FriendDTO> {
		return when (sortBy.value) {
			SortBy.Nickname -> {
				when (sortOrder.value) {
					SortOrder.ASC -> this.sortedBy { it.pinyin.lowercase() }
					SortOrder.DESC -> this.sortedByDescending { it.pinyin.lowercase() }
				}
			}
			
			SortBy.CreateTime -> {
				when (sortOrder.value) {
					SortOrder.ASC -> this.sortedBy { it.createTime }
					SortOrder.DESC -> this.sortedByDescending { it.createTime }
				}
			}
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
	
	enum class SortOrder {
		ASC,
		DESC
	}
	
	enum class SortBy {
		Nickname,
		CreateTime
	}
}

object FriendVersionConfigKey : ConfigKey<Int>("friendVersion")