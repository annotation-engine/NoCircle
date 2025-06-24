package com.nocircle.app.pages.main.friends.list

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.friendApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.room.AppDatabase
import com.nocircle.app.room.entity.FriendListEntity
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.UserIdConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.expends.findIndices
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.model.friend.FriendDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FriendListViewModel : NoViewModel() {
	
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	
	private val _friendList = MutableStateFlow<List<FriendDTO>>(emptyList())
	val friendList = _friendList.asStateFlow()
	
	val sortOrder = MutableStateFlow(SortOrder.ASC)
	
	private val _friendSearchList = MutableStateFlow<List<FriendSearch>>(emptyList())
	val friendSearchList = _friendSearchList.asStateFlow()
	
	private val _showFriendSearchList = MutableStateFlow(false)
	val showFriendSearchList = _showFriendSearchList.asStateFlow()
	
	init {
		viewModelScope.launch {
			async { loadFriendList() }
			async { sortOrderCollect() }
			async { searchCollect() }
		}
	}
	
	suspend fun loadFriendList() {
		val userId = UserIdConfigKey.get()!!
		val friendVersionResult = ktorfitx.friendApi.queryFriendVersion() ?: return networkError()
		if (!friendVersionResult.success) {
			return showNoErrorSnackbar(friendVersionResult.msg)
		}
		val friendListDao = AppDatabase.INSTANCE.getFriendListDao()
		
		val version = FriendVersionConfigKey.get() ?: -1
		
		val friendList = if (friendVersionResult.data!! == version) {
			friendListDao.queryList(userId).map {
				FriendDTO(
					friendId = it.friendId,
					username = it.username,
					nickname = it.nickname,
					avatarUrl = it.avatarUrl,
					pinyin = it.pinyin
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
					pinyin = it.pinyin
				)
				friendListDao.insert(entity)
			}
			result.data!!
		}
		_friendList.value = friendList.sorted()
	}
	
	private suspend fun sortOrderCollect() {
		sortOrder.collect {
			_friendList.value = friendList.value.sorted()
		}
	}
	
	@OptIn(FlowPreview::class)
	private suspend fun searchCollect() {
		search.debounce(0.2.seconds)
			.collectLatest {
				val search = it.trim()
				_showFriendSearchList.value = search.isNotEmpty()
				if (!_showFriendSearchList.value) {
					return@collectLatest
				}
				_friendSearchList.value = friendList.value.mapNotNull {
					val usernameIndices = it.username.findIndices(search, ignoreCase = true)
					val nicknameIndices = it.nickname.findIndices(search, ignoreCase = true)
					if (usernameIndices.isEmpty() && nicknameIndices.isEmpty()) {
						return@mapNotNull null
					}
					FriendSearch(
						friendId = it.friendId,
						username = it.username,
						nickname = it.nickname,
						avatarUrl = it.avatarUrl,
						usernameIndices = usernameIndices,
						nicknameIndices = nicknameIndices
					)
				}.sortedWith { item1, item2 ->
					val count1 = item1.usernameIndices.size + item1.nicknameIndices.size
					val count2 = item2.usernameIndices.size + item2.nicknameIndices.size
					when {
						count1 > count2 -> -1
						count1 < count2 -> 1
						else -> 0
					}
				}
			}
	}
	
	private fun List<FriendDTO>.sorted(): List<FriendDTO> {
		return if (sortOrder.value == SortOrder.ASC) {
			this.sortedBy { it.pinyin.lowercase() }
		} else {
			this.sortedByDescending { it.pinyin.lowercase() }
		}
	}
	
	fun updateSearch(search: String) {
		if (search.length <= 20) {
			_search.value = search
		}
	}
	
	enum class SortOrder { ASC, DESC }
	
	data class FriendSearch(
		val friendId: Int,
		val username: String,
		val nickname: String,
		val avatarUrl: String?,
		val usernameIndices: List<IntRange>,
		val nicknameIndices: List<IntRange>,
	)
}

object FriendVersionConfigKey : ConfigKey<Int>("friendVersion")