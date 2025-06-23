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
	val leftWidth by viewModel.leftWidth.collectAsState()
	NoAdaptiveSplitScreen(
		leftWidth = leftWidth,
		onLeftWidthChange = { viewModel.leftWidth.value = it },
		leftContent = { navigate ->
			FriendList(navigate)
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