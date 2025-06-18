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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.LocalSnackbarHostState
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.resources.value
import com.nocircle.shared.model.friend.FriendSearchDTO
import com.nocircle.shared.model.friend.FriendSearchDTO.RelationshipDTO.*
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
			viewModel.init()
			viewModel.snackbarCollect(hostState::showNoSnackbar)
		}
		val username by viewModel.search.collectAsState()
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
	searchUser: FriendSearchDTO
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainerHigh,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
			.height(100.dp)
	) {
		NoAsyncImage(
			url = searchUser.avatarUrl,
			modifier = Modifier
				.size(100.dp)
				.clip(MaterialTheme.shapes.medium),
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
					text = searchUser.nickname ?: AppString.FRIENDS_ADD_FRIEND_NOT_NICKNAME.value(),
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FRIENDS_ADD_FRIEND_ID.value(searchUser.username),
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			val icon by remember(searchUser.relationship) {
				derivedStateOf {
					when (searchUser.relationship) {
						FRIEND -> AppIcon.Group
						OWNER -> AppIcon.Person
						STRANGER -> AppIcon.GroupAdd
					}
				}
			}
			NoIcon(
				icon = icon.value(),
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
					NoTag(
						text = label.label,
						color = hexToColor(label.color),
						contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
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
		mutableStateOf(searchUser.relationship == STRANGER && !searchUser.isAlreadySend)
	}
	var string by remember(searchUser) {
		mutableStateOf(
			value = when (searchUser.relationship) {
				FRIEND -> AppString.FRIENDS_ADD_FRIEND_ALREADY_FRIEND
				OWNER -> AppString.FRIENDS_ADD_FRIEND_NOT_ADD_OWNER
				STRANGER -> when (searchUser.isAlreadySend) {
					true -> AppString.FRIENDS_ADD_FRIEND_ALREADY_SEND_REQUEST
					false -> AppString.FRIENDS_ADD_FRIEND_SEND_REQUEST
				}
			}
		)
	}
	NoButton(
		text = string.value(),
		modifier = Modifier.fillMaxWidth(),
		enabled = enabled,
		colors = NoButtonColors.PrimaryColors
	) {
		val success = viewModel.sendFriendAddRequest(searchUser.userId)
		if (success) {
			string = AppString.FRIENDS_ADD_FRIEND_SEND_SUCCESS
			enabled = false
		}
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