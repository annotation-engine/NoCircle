package com.nocircle.app.pages.friends.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.main.navigation.AutoVisibleBottomNavigation
import com.nocircle.app.resources.AppIcon
import com.nocircle.common.constants.StringConstants
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import com.nocircle.shared.model.friend.FriendDetailDTO
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendDetailPage(
	friendId: Int?,
	popBackStack: () -> Unit
) {
	val viewModel = koinViewModel<FriendDetailViewModel>()
	LaunchedEffect(friendId) {
		if (friendId != null) {
			viewModel.loadFriendDetail(friendId)
		}
	}
	AutoVisibleBottomNavigation()
	NoScaffold(
		topBar = {
			val isCompact = WindowWidthSizes.isCompact
			NoTopAppBar(
				navigationIcon = {
					NoIconButton(
						icon = if (isCompact) AppIcon.ArrowBack.value() else AppIcon.Close.value(),
						containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
					) {
						popBackStack()
					}
				},
				contentPadding = if (isCompact) NoTopAppBarDefaults.MediumContentPadding else NoTopAppBarDefaults.SmallContentPadding,
				colors = NoTopAppBarDefaults.TransparentTopAppColors
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize(),
			contentAlignment = Alignment.TopCenter
		) {
			val friendDetail by viewModel.friendDetail.collectAsState()
			if (friendDetail != null) {
				FriendAvatarBackground(
					avatarUrl = friendDetail!!.avatarUrl
				)
				FriendDetailColumn(
					item = friendDetail!!
				)
			}
		}
	}
}

@Composable
private fun FriendAvatarBackground(
	avatarUrl: String?
) {
	NoAsyncImage(
		url = avatarUrl,
		modifier = Modifier
			.fillMaxWidth()
			.height(220.dp)
			.blur(50.dp)
	)
}

@Composable
private fun FriendDetailColumn(
	item: FriendDetailDTO
) {
	Column(
		modifier = Modifier
			.padding(top = 170.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		NoAsyncImage(
			url = item.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.border(
					width = 2.5.dp,
					color = MaterialTheme.colorScheme.surface,
					shape = MaterialTheme.shapes.large
				)
				.clip(MaterialTheme.shapes.large)
		)
		Spacer(modifier = Modifier.height(16.dp))
		Text(
			text = item.nickname,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleLarge
		)
		Spacer(modifier = Modifier.height(8.dp))
		Text(
			text = StringConstants.ID + item.username,
			color = MaterialTheme.colorScheme.outline,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}