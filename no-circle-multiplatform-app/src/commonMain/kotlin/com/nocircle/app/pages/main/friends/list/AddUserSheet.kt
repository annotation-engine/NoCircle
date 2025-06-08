package com.nocircle.app.pages.main.friends.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.nocircle.app.api.SearchUserVO
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.NoModalBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserSheet(
	onDismissRequest: () -> Unit,
) {
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
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
		Spacer(modifier = Modifier.height(32.dp))
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(300.dp),
			contentAlignment = Alignment.TopCenter
		) {
			val user by viewModel.result.collectAsState()
			if (user != null) {
				UserCard(user!!)
			} else {
				Text(
					text = if (username.isBlank()) {
						AppString.FriendsSearchPleaseInputUsername.value()
					} else {
						AppString.FriendsSearchNotFoundUser.value()
					},
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.outline
				)
			}
		}
	}
}

@Composable
private fun UserCard(
	user: SearchUserVO
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(100.dp)
		) {
			NoAsyncImage(
				url = user.avatarUrl,
				modifier = Modifier
					.size(100.dp)
					.clip(MaterialTheme.shapes.small),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.width(12.dp))
			Column(
				modifier = Modifier
					.fillMaxSize()
			) {
				Text(
					text = user.nickname ?: AppString.FriendsSearchNotNickname.value(),
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FriendsID.value(user.username),
					style = MaterialTheme.typography.bodyMedium,
				)
				Spacer(modifier = Modifier.weight(1f))
			}
		}
	}
}