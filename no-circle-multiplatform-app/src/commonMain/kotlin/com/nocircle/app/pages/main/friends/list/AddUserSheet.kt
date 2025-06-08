package com.nocircle.app.pages.main.friends.list

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
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
import com.nocircle.app.api.SearchUserVO
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.format
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserSheet(
	onDismissRequest: () -> Unit,
) {
	val sheetState = rememberNoModalBottomSheetState()
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
		sheetState = sheetState,
		icon = { NoIcon(AppIcon.Add.value) },
		title = { Text(AppString.FriendsSearchTitle.value()) }
	) {
		val viewModel = koinViewModel<AddUserSheetViewModel>()
		val username by viewModel.username.collectAsState()
		NoTextField(
			value = username,
			onValueChange = viewModel::updateSearch,
			modifier = Modifier.fillMaxWidth(),
			leadingIcon = { NoIcon(AppIcon.Search.value) },
			placeholder = { Text(AppString.FriendsSearchHint.value()) }
		)
		Spacer(modifier = Modifier.height(16.dp))
		HorizontalDivider()
		Spacer(modifier = Modifier.height(16.dp))
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.height(297.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val searchUser by viewModel.result.collectAsState()
			if (searchUser != null) {
				UserCard(
					searchUser = searchUser!!,
					sheetState = sheetState,
					onDismissRequest = onDismissRequest,
				)
			} else {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(60.dp)
						.background(
							color = MaterialTheme.colorScheme.surfaceContainerHigh,
							shape = MaterialTheme.shapes.small
						),
					contentAlignment = Alignment.Center
				) {
					val hintString by remember(username) {
						derivedStateOf {
							if (username.isBlank()) AppString.FriendsSearchPleaseInputUsername else AppString.FriendsSearchNotFoundUser
						}
					}
					Text(
						text = hintString.value(),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.outline
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCard(
	searchUser: SearchUserVO,
	sheetState: SheetState,
	onDismissRequest: () -> Unit,
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
					text = searchUser.nickname ?: AppString.FriendsSearchNotNickname.value(),
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FriendsID.value().format(searchUser.username),
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
				searchUser.labels.fastForEachIndexed { index, label ->
					Label(
						label = label.label,
						color = label.color,
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
	val viewModel = koinViewModel<AddUserSheetViewModel>()
	NoButton(
		text = "发送好友申请",
		modifier = Modifier
			.fillMaxWidth(),
		colors = NoButtonColors.PrimaryColors
	) {
		viewModel.sendFriendRequest(searchUser.userId)
	}
	Spacer(modifier = Modifier.height(16.dp))
	NoButton(
		text = "取消",
		modifier = Modifier
			.fillMaxWidth(),
		colors = NoButtonColors.SurfaceContainerHighColors
	) {
		sheetState.hide()
		onDismissRequest()
	}
}

@Composable
private fun Label(
	label: String,
	color: Int,
) {
	val color = Color(color)
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