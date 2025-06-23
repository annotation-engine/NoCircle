package com.nocircle.app.pages.main.person.message

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.*
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MessageCenterRoute : NoRoute

@Composable
fun MessageCenterPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.MESSAGE_CENTER_TITLE.value()) },
				navigationIcon = if (WindowWidthSizes.isCompact) {
					{
						NoIconButton(
							icon = AppIcon.ArrowBack.value()
						) {
							controller.popBackStack()
						}
					}
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
				val viewModel = koinViewModel<MessageCenterViewModel>()
				var selectedSubRoute by remember { mutableStateOf(MessageCenterSubRoute.SENT_REQUEST) }
				val sentRequests by viewModel.sentRequests.collectAsState()
				val receivedRequests by viewModel.receivedRequests.collectAsState()
				NoTabRow(
					selected = selectedSubRoute,
					onSelectedChange = { selectedSubRoute = it },
					items = MessageCenterSubRoute.entries,
					interval = 12.dp
				) { subRoute ->
					val icon by remember(subRoute, receivedRequests.size) {
						derivedStateOf {
							when {
								subRoute == MessageCenterSubRoute.SENT_REQUEST -> AppIcon.ForwardToInbox
								receivedRequests.isEmpty() -> AppIcon.Email
								else -> AppIcon.MarkEmailUnread
							}
						}
					}
					NoIcon(icon.value())
					Spacer(modifier = Modifier.width(8.dp))
					val count = if (subRoute == MessageCenterSubRoute.SENT_REQUEST) sentRequests.size else receivedRequests.size
					Text(subRoute.title.value(count))
				}
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
						MessageCenterSubRoute.SENT_REQUEST -> MessageCenterSentRequestList()
						MessageCenterSubRoute.RECEIVED_REQUEST -> MessageCenterReceivedRequestList()
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
fun NoMessage() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(80.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = AppString.MESSAGE_CENTER_NO_MESSAGES.value(),
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			style = MaterialTheme.typography.bodyLarge
		)
	}
}

private enum class MessageCenterSubRoute(
	val title: AppString
) {
	SENT_REQUEST(AppString.MESSAGE_CENTER_SENT_REQUEST),
	RECEIVED_REQUEST(AppString.MESSAGE_CENTER_RECEIVED_REQUEST)
}