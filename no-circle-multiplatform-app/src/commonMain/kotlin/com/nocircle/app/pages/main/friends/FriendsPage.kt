package com.nocircle.app.pages.main.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nocircle.app.pages.main.friends.list.FriendsList
import com.nocircle.compose.layout.NoSplitLayout
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsPage() {
	val viewModel = koinViewModel<FriendsViewModel>()
	val contentWidth by viewModel.contentWidth.collectAsState()
	NoSplitLayout(
		contentWidth = contentWidth,
		onContentWidthChange = { viewModel.contentWidth.value = it },
		expended = {
		
		}
	) { isCompat ->
		FriendsList(isCompat)
	}
}