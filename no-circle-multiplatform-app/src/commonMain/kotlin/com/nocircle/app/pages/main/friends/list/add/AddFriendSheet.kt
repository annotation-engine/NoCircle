package com.nocircle.app.pages.main.friends.list.add

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.api.RelationshipVO
import com.nocircle.app.api.SearchUserVO
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.format
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.LocalSnackbarHostState
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import com.nocircle.compose.material3.showNoSnackbar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendSheet(
	onDismissRequest: () -> Unit,
) {
	val sheetState = rememberNoModalBottomSheetState()
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
		sheetState = sheetState,
		icon = { NoIcon(AppIcon.Add.value()) },
		title = { Text(AppString.FRIENDS_ADD_FRIEND_TITLE.value()) }
	) {
		val viewModel = koinViewModel<AddFriendViewModel>()
		val hostState = LocalSnackbarHostState.current
		LaunchedEffect(Unit) {
			viewModel.snackbarCollect(hostState::showNoSnackbar)
		}
		val username by viewModel.username.collectAsState()
		NoTextField(
			value = username,
			onValueChange = viewModel::updateSearch,
			modifier = Modifier.fillMaxWidth(),
			leadingIcon = { NoIcon(AppIcon.Search.value()) },
			placeholder = { Text(AppString.FRIENDS_ADD_FRIEND_HINT.value()) }
		)
		Spacer(modifier = Modifier.height(16.dp))
		HorizontalDivider()
		Spacer(modifier = Modifier.height(16.dp))
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.height(221.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val searchUser by viewModel.result.collectAsState()
			if (searchUser != null) {
				UserCard(
					searchUser = searchUser!!
				)
			} else {
				val hint by remember(username) {
					derivedStateOf {
						if (username.isBlank()) AppString.FRIENDS_ADD_FRIEND_PLEASE_INPUT_USERNAME else AppString.FRIENDS_ADD_FRIEND_NOT_FOUND_USER
					}
				}
				HintText(
					value = hint.value()
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCard(
	searchUser: SearchUserVO
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainerHigh,
				shape = MaterialTheme.shapes.small
			)
			.padding(16.dp)
			.height(100.dp)
	) {
		NoAsyncImage(
			url = searchUser.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.clip(MaterialTheme.shapes.small),
			placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceDim),
			contentScale = ContentScale.Crop
		)
		Spacer(modifier = Modifier.width(16.dp))
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column {
				Text(
					text = searchUser.nickname ?: AppString.FRIENDS_ADD_FRIEND_NOT_NICKNAME.value(),
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FRIENDS_ADD_FRIEND_ID.value().format(searchUser.username),
					color = MaterialTheme.colorScheme.outline,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			val iconGroup by remember(searchUser.relationship) {
				derivedStateOf {
					when (searchUser.relationship) {
						RelationshipVO.FRIEND -> AppIcon.Group
						RelationshipVO.OWNER -> AppIcon.Person
						RelationshipVO.STRANGER -> AppIcon.GroupAdd
					}
				}
			}
			NoIcon(
				icon = iconGroup.value(),
				modifier = Modifier.align(Alignment.TopEnd),
				tint = MaterialTheme.colorScheme.primary
			)
			
			val horizontalScroll = rememberScrollState()
			Row(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.horizontalScroll(horizontalScroll)
					.height(24.dp)
			) {
				searchUser.labels.fastForEachIndexed { index, label ->
					Label(
						label = label.label,
						color = hexToColor(label.color),
					)
					if (index < searchUser.labels.lastIndex) {
						Spacer(modifier = Modifier.width(6.dp))
					}
				}
			}
		}
	}
	Spacer(modifier = Modifier.height(16.dp))
	HorizontalDivider()
	Spacer(modifier = Modifier.height(16.dp))
	val viewModel = koinViewModel<AddFriendViewModel>()
	var enabled by remember(searchUser.relationship, searchUser.isAlreadySend) {
		mutableStateOf(searchUser.relationship == RelationshipVO.STRANGER && !searchUser.isAlreadySend)
	}
	var buttonString by remember(searchUser.relationship, searchUser.isAlreadySend) {
		mutableStateOf(
			value = when (searchUser.relationship) {
				RelationshipVO.FRIEND -> AppString.FRIENDS_ADD_FRIEND_ALREADY_FRIEND
				RelationshipVO.OWNER -> AppString.FRIENDS_ADD_FRIEND_NOT_ADD_OWNER
				RelationshipVO.STRANGER -> when (searchUser.isAlreadySend) {
					true -> AppString.FRIENDS_ADD_FRIEND_ALREADY_SEND_REQUEST
					false -> AppString.FRIENDS_ADD_FRIEND_SEND_REQUEST
				}
			}
		)
	}
	NoButton(
		text = buttonString.value(),
		modifier = Modifier.fillMaxWidth(),
		enabled = enabled,
		colors = NoButtonColors.PrimaryColors
	) {
		val success = viewModel.sendFriendAddRequest(searchUser.userId)
		if (success) {
			buttonString = AppString.FRIENDS_ADD_FRIEND_SEND_SUCCESS
			enabled = false
		}
	}
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

@Composable
private fun HintText(
	value: String
) {
	Text(
		text = value,
		style = MaterialTheme.typography.bodyLarge,
		color = MaterialTheme.colorScheme.outline
	)
}