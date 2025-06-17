package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.main.person.label.EditLabelSheet
import com.nocircle.app.pages.main.person.message.MessageCenterRoute
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTag
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.resources.value
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val verticalScrollState = rememberScrollState()
	NoScaffold { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize(),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 840.dp)
					.fillMaxSize()
					.verticalScroll(verticalScrollState)
					.padding(paddingValues)
					.padding(16.dp)
			) {
				UserDetailCard()
				Spacer(modifier = Modifier.height(16.dp))
				LastLoginTime()
				Spacer(modifier = Modifier.height(16.dp))
				FriendAddRequest()
				Spacer(modifier = Modifier.height(16.dp))
				OptionList()
			}
		}
	}
}

@Composable
private fun UserDetailCard() {
	val viewModel = koinViewModel<PersonViewModel>()
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
		val userDetail by viewModel.userDetail.collectAsState()
		NoAsyncImage(
			url = userDetail?.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.clip(MaterialTheme.shapes.small),
			placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceDim),
			contentScale = ContentScale.Crop
		)
		Spacer(modifier = Modifier.width(12.dp))
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column {
				Text(
					text = userDetail?.nickname ?: "",
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.PERSON_ID.value(userDetail?.username ?: ""),
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
						color = if (userDetail == null) MaterialTheme.colorScheme.surface else Color.Transparent,
						shape = MaterialTheme.shapes.extraSmall
					)
			) {
				val labels by viewModel.labels.collectAsState()
				labels.let {
					it.fastForEachIndexed { index, label ->
						NoTag(
							text = label.label,
							color = hexToColor(label.color),
							shape = MaterialTheme.shapes.extraSmall,
							contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
						)
						Spacer(modifier = Modifier.width(6.dp))
					}
					EditLabel(
						icon = if (it.size < 4) AppIcon.Add.value() else AppIcon.Remove.value(),
					)
				}
			}
		}
	}
}

@Composable
private fun EditLabel(
	icon: ImageVector
) {
	var showModal by remember { mutableStateOf(false) }
	NoIconButton(
		icon = icon,
		modifier = Modifier
			.size(24.dp),
		tint = MaterialTheme.colorScheme.onSurfaceVariant,
		shape = MaterialTheme.shapes.extraSmall,
		contentPadding = PaddingValues()
	) {
		showModal = true
	}
	if (showModal) {
		EditLabelSheet(
			onDismissRequest = { showModal = false }
		)
	}
}

@Composable
private fun LastLoginTime() {
	val viewModel = koinViewModel<PersonViewModel>()
	val userDetail by viewModel.userDetail.collectAsState()
	val lastLoginTime = userDetail?.lastLoginTime
	NoOption(
		title = { Text(AppString.PERSON_LAST_LOGIN_TIME.value()) },
		icon = { NoIcon(AppIcon.AccessTime.value()) },
		actions = {
			Text(
				text = lastLoginTime ?: "-----",
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		},
		showSuffixIcon = false
	)
}

@Composable
private fun FriendAddRequest() {
	val controller = LocalNavController.current
	val viewModel = koinViewModel<PersonViewModel>()
	val waitingRequestCount by viewModel.waitingRequestCount.collectAsState()
	NoOption(
		title = { Text(AppString.MESSAGE_CENTER_TITLE.value()) },
		icon = {
			val icon by remember(waitingRequestCount) {
				derivedStateOf {
					if (waitingRequestCount == 0) AppIcon.Email else AppIcon.MarkEmailUnread
				}
			}
			NoIcon(icon.value())
		},
		actions = {
			Text(
				text = if (waitingRequestCount == 0) AppString.PERSON_MESSAGE_CENTER_SUBTITLE_NO_NEWS.value() else AppString.PERSON_MESSAGE_CENTER_SUBTITLE_NEWS.value(waitingRequestCount),
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		}
	) {
		controller.navigate(route = MessageCenterRoute)
	}
}

@Composable
private fun OptionList() {
	val controller = LocalNavController.current
	NoOption(
		title = { Text(AppString.SETTINGS.value()) },
		icon = { NoIcon(AppIcon.Settings.value()) },
		actions = {
			Text(
				text = AppString.PERSON_SETTINGS_SUBTITLE.value(),
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		}
	) {
		controller.navigate(route = SettingsRoute)
	}
}