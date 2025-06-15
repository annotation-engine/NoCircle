package com.nocircle.app.pages.main.person.message

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.NoIcons
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
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
fun MessageCenterLabel(
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