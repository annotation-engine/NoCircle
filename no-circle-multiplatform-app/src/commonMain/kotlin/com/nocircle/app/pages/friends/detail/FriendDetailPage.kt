package com.nocircle.app.pages.friends.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.main.navigation.AutoVisibleBottomNavigation
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.constants.StringConstants
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTag
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
			.height(180.dp)
			.blur(50.dp)
	)
}

@Composable
private fun FriendDetailColumn(
	item: FriendDetailDTO
) {
	Column(
		modifier = Modifier
			.padding(top = 127.5.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		NoAsyncImage(
			url = item.avatarUrl,
			modifier = Modifier
				.border(
					width = 2.5.dp,
					color = MaterialTheme.colorScheme.surface,
					shape = MaterialTheme.shapes.medium
				)
				.size(100.dp)
				.clip(MaterialTheme.shapes.medium)
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
		Spacer(modifier = Modifier.height(16.dp))
		Row {
			item.labels.fastForEachIndexed { index, label ->
				NoTag(
					text = label.label,
					color = hexToColor(label.color)
				)
				if (index < item.labels.lastIndex) {
					Spacer(modifier = Modifier.width(6.dp))
				}
			}
			val showNoLabel by remember(item.labels) {
				derivedStateOf { item.labels.isEmpty() }
			}
			if (showNoLabel) {
				NoTag(
					text = AppString.PERSON_MESSAGE_NO_LABEL.value(),
					color = MaterialTheme.colorScheme.surfaceContainerHighest
				)
			}
		}
		Spacer(modifier = Modifier.height(16.dp))
		NoButton(
			text = "发送消息",
			modifier = Modifier
				.width(120.dp)
				.height(48.dp)
		) {
		
		}
	}
}