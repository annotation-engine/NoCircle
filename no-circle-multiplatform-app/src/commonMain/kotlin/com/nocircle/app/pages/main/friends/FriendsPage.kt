package com.nocircle.app.pages.main.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nocircle.app.pages.main.friends.detail.FriendDetailPage
import com.nocircle.app.pages.main.friends.list.FriendList
import com.nocircle.compose.layout.NoAdaptiveSplitScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsPage() {
	val viewModel = koinViewModel<FriendsViewModel>()
	val current by viewModel.current.collectAsState()
	val leftWidth by viewModel.leftWidth.collectAsState()
	NoAdaptiveSplitScreen(
		current = current,
		onCurrentChange = { viewModel.current.value = it },
		leftWidth = leftWidth,
		onLeftWidthChange = { viewModel.leftWidth.value = it },
		leftContent = { current, navigate ->
			FriendList(
				current = current,
				navigate = navigate
			)
		},
		rightContent = { friendId, popBackStack ->
			FriendDetailPage(
				friendId = friendId,
				popBackStack = popBackStack
			)
		},
		rightEmptyContent = {
		
		}
	)
}