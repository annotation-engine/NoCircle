package com.nocircle.app.pages.main.friends.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.main.hideBottomNavigationBar
import com.nocircle.app.pages.main.showBottomNavigationBar
import com.nocircle.app.resources.AppIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.layout.VerticalScrollColumn
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendDetailPage(
	friendId: Int,
	popBackStack: () -> Unit
) {
	val isCompact = WindowWidthSizes.isCompact
	val viewModel = koinViewModel<FriendDetailViewModel>()
	LaunchedEffect(friendId) {
		viewModel.loadFriendDetail(friendId)
	}
	DisposableEffect(Unit) {
		if (isCompact) {
			hideBottomNavigationBar()
		}
		onDispose {
			showBottomNavigationBar()
		}
	}
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = {
					val friendDetail by viewModel.friendDetail.collectAsState()
					Text(
						text = friendDetail?.nickname ?: ""
					)
				},
				navigationIcon = {
					NoIconButton(
						icon = if (isCompact) AppIcon.ArrowBack.value() else AppIcon.Close.value()
					) {
						popBackStack()
					}
				},
				contentPadding = if (isCompact) NoTopAppBarDefaults.contentPadding else MediumContentPadding
			)
		}
	) { paddingValues ->
		VerticalScrollColumn(
			modifier = Modifier.padding(paddingValues)
		) {
			FriendDetail(
				viewModel = viewModel,
			)
		}
	}
}

@Composable
private fun FriendDetail(
	viewModel: FriendDetailViewModel
) {
	val friendDetail by viewModel.friendDetail.collectAsState()
	Text(
		text = friendDetail.toString()
	)
}

private val MediumContentPadding = PaddingValues(12.dp)