package com.nocircle.app.pages.main.person.message

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.api.FriendRequestDTO
import com.nocircle.app.api.FriendRequestDTO.RequestStatus.*
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.NoColor
import com.nocircle.common.expends.format
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.NoIcons
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.*
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MessageCenterRoute : NoRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCenterPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.MESSAGE_CENTER_TITLE.value()) },
				onNavigationIconClick = if (WindowWidthSizes.isCompact) {
					{ controller.popBackStack() }
				} else null
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = paddingValues.calculateTopPadding()),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 840.dp)
					.fillMaxSize()
					.padding(
						start = 16.dp,
						top = 16.dp,
						end = 16.dp
					)
			) {
				var selectedSubRoute by remember { mutableStateOf(MessageCenterSubRoute.SENT_REQUEST) }
				NoTabRow(
					selectedTabIndex = selectedSubRoute.ordinal
				) {
					MessageCenterSubRoute.entries.fastForEach {
						NoTab(
							selected = selectedSubRoute == it,
							onClick = { selectedSubRoute = it }
						) {
							NoIcon(it.iconGroup.value())
							Spacer(modifier = Modifier.width(8.dp))
							Text(it.title.value())
						}
					}
				}
				val viewModel = koinViewModel<MessageCenterViewModel>()
				val hostState = LocalSnackbarHostState.current
				LaunchedEffect(Unit) {
					viewModel.snackbarCollect(hostState::showNoSnackbar)
				}
				Crossfade(
					targetState = selectedSubRoute,
					modifier = Modifier.fillMaxSize(),
					animationSpec = tween(durationMillis = 100),
					label = "MessageCenterCrossfade",
				) { subRoute ->
					when (subRoute) {
						MessageCenterSubRoute.SENT_REQUEST -> {
							val sentRequests by viewModel.sentRequests.collectAsState()
							RequestPage(
								requests = sentRequests,
								isSent = true
							)
						}
						
						MessageCenterSubRoute.RECEIVED_REQUEST -> {
							val receivedRequests by viewModel.receivedRequests.collectAsState()
							RequestPage(
								requests = receivedRequests,
								isSent = false
							)
						}
					}
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(16.dp)
							.background(
								brush = Brush.verticalGradient(
									0f to MaterialTheme.colorScheme.surface,
									1f to MaterialTheme.colorScheme.surface.copy(alpha = 0f)
								)
							)
					)
				}
			}
		}
	}
}

@Composable
private fun RequestPage(
	requests: List<FriendRequestDTO.RequestDTO>,
	isSent: Boolean
) {
	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		itemsIndexed(
			items = requests,
			key = { _, it -> it.id }
		) { index, request ->
			Spacer(Modifier.height(16.dp))
			RequestCard(
				request = request,
				isSent = isSent
			)
			if (index == requests.lastIndex) {
				Spacer(Modifier.height(16.dp))
			}
		}
	}
}

@Composable
private fun RequestCard(
	request: FriendRequestDTO.RequestDTO,
	isSent: Boolean
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.small)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
			.height(100.dp)
	) {
		NoAsyncImage(
			url = request.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.clip(MaterialTheme.shapes.small),
			contentScale = ContentScale.Crop
		)
		Spacer(modifier = Modifier.width(12.dp))
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column {
				Text(
					text = request.nickname ?: AppString.FRIENDS_ADD_FRIEND_NOT_NICKNAME.value(),
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FRIENDS_ADD_FRIEND_ID.value().format(request.username),
					color = MaterialTheme.colorScheme.outline,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			val horizontalScroll = rememberScrollState()
			Row(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.horizontalScroll(horizontalScroll)
					.height(24.dp)
			) {
				request.labels.fastForEachIndexed { index, label ->
					Label(
						label = label.label,
						color = hexToColor(label.color),
					)
					if (index < request.labels.lastIndex) {
						Spacer(modifier = Modifier.width(6.dp))
					}
				}
			}
			if (isSent) {
				val color = request.status.getColor()
				Text(
					text = request.status.getString(),
					modifier = Modifier
						.align(Alignment.TopEnd)
						.clip(MaterialTheme.shapes.small)
						.border(
							width = 1.dp,
							color = color,
							shape = MaterialTheme.shapes.small
						)
						.padding(
							horizontal = 12.dp,
							vertical = 8.dp
						),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyMedium,
					color = color
				)
			}
		}
	}
}

@Composable
private fun FriendRequestDTO.RequestStatus.getColor(): Color {
	return when (this) {
		AGREED -> NoColor.Green
		REJECTED -> NoColor.Red
		WAITING -> NoColor.Yellow
		CANCELED -> NoColor.Gray
	}
}

@Composable
private fun FriendRequestDTO.RequestStatus.getString(): String {
	return when (this) {
		AGREED -> AppString.MESSAGE_CENTER_AGREED
		REJECTED -> AppString.MESSAGE_CENTER_REJECTED
		WAITING -> AppString.MESSAGE_CENTER_WAITING
		CANCELED -> AppString.MESSAGE_CENTER_CANCELED
	}.value()
}

@Composable
private fun Label(
	label: String,
	color: Color,
) {
	Box(
		modifier = Modifier
			.fillMaxHeight()
			.background(
				color = color,
				shape = MaterialTheme.shapes.extraSmall
			)
			.padding(horizontal = 6.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = label,
			color = if (color.luminance() > 0.5f) Color.Black else Color.White,
			style = MaterialTheme.typography.labelMedium
		)
	}
}

private enum class MessageCenterSubRoute(
	val title: AppString,
	val iconGroup: NoIcons
) {
	SENT_REQUEST(AppString.MESSAGE_CENTER_SENT_REQUEST, AppIcon.ForwardToInbox),
	RECEIVED_REQUEST(AppString.MESSAGE_CENTER_RECEIVED_REQUEST, AppIcon.MarkEmailUnread)
}