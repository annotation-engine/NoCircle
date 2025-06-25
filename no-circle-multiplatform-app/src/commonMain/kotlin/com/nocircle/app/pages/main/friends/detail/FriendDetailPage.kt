package com.nocircle.app.pages.main.friends.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.main.hideBottomNavigationBar
import com.nocircle.app.pages.main.showBottomNavigationBar
import com.nocircle.app.resources.AppIcon
import com.nocircle.common.constants.StringConstants
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTag
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
			FriendDetailCard()
		}
	}
}

@Composable
private fun FriendDetailCard() {
	val viewModel = koinViewModel<FriendDetailViewModel>()
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
			.height(100.dp)
	) {
		val friendDetail by viewModel.friendDetail.collectAsState()
		NoAsyncImage(
			url = friendDetail?.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.clip(MaterialTheme.shapes.medium),
			placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest),
			contentScale = ContentScale.Crop
		)
		Spacer(modifier = Modifier.width(12.dp))
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column {
				Text(
					text = friendDetail?.nickname ?: "",
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = StringConstants.ID + (friendDetail?.username ?: ""),
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			
			val horizontalScroll = rememberScrollState()
			Row(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.horizontalScroll(horizontalScroll)
					.background(
						color = if (friendDetail == null) MaterialTheme.colorScheme.surface else Color.Transparent,
						shape = MaterialTheme.shapes.extraSmall
					)
			) {
				val labels = friendDetail?.labels
				labels?.fastForEachIndexed { index, label ->
					NoTag(
						text = label.label,
						color = hexToColor(label.color),
						shape = MaterialTheme.shapes.small,
						contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
					)
					if (index < labels.lastIndex) {
						Spacer(modifier = Modifier.width(6.dp))
					}
				}
			}
		}
	}
}

private val MediumContentPadding = PaddingValues(12.dp)