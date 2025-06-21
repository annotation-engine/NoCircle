package com.nocircle.app.pages.main.friends.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.main.friends.list.add.AddFriendSheet
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.device.DeviceType
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import com.nocircle.compose.resources.value
import com.nocircle.shared.model.friend.FriendDTO
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsList(
	isCompat: Boolean,
) {
	val viewModel = koinViewModel<FriendsListViewModel>()
	NoScaffold(
		topBar = {
			NoTopAppBar(
				actions = {
					FriendsSearch(
						viewModel = viewModel,
						isCompat = isCompat,
					)
				},
				contentPadding = if (isCompat) NoTopAppBarDefaults.contentPadding else MediumContentPadding
			)
		}
	) { paddingValues ->
		val friendList by viewModel.friendList.collectAsState()
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			contentPadding = PaddingValues(vertical = 8.dp)
		) {
			itemsIndexed(
				items = friendList,
				key = { _, it -> it.userId }
			) { index, it ->
				val showFirst = index == 0 || friendList[index - 1].first != it.first
				if (showFirst) {
					FirstChar(it.first)
				}
				Friend(it)
			}
		}
	}
}

private val MediumContentPadding = PaddingValues(12.dp)

@Composable
private fun FriendsSearch(
	viewModel: FriendsListViewModel,
	isCompat: Boolean,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		val search by viewModel.search.collectAsState()
		val size by remember(isCompat) {
			derivedStateOf {
				when {
					DeviceType.isDesktop -> if (isCompat) 48.dp else 40.dp
					else -> if (isCompat) 48.dp else 44.dp
				}
			}
		}
		NoTextField(
			value = search,
			onValueChange = viewModel::updateSearch,
			modifier = Modifier
				.weight(1f)
				.height(size),
			prefix = { NoIcon(icon = AppIcon.Search.value()) },
			placeholder = {
				Text(
					text = AppString.FRIENDS_SEARCH.value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
		Spacer(modifier = Modifier.width(if (isCompat) 12.dp else 8.dp))
		var showAddFriendSheet by remember { mutableStateOf(false) }
		NoIconButton(
			icon = AppIcon.Add.value(),
			modifier = Modifier.size(size),
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
			containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
		) {
			showAddFriendSheet = true
		}
		if (showAddFriendSheet) {
			AddFriendSheet(
				onDismissRequest = {
					showAddFriendSheet = false
				}
			)
		}
	}
}

@Composable
private fun Friend(
	friend: FriendDTO,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				start = 32.dp,
				top = 8.dp,
				end = 16.dp,
				bottom = 8.dp
			)
			.height(40.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		NoAsyncImage(
			url = friend.avatarUrl,
			modifier = Modifier
				.size(40.dp)
				.clip(MaterialTheme.shapes.medium),
			contentScale = ContentScale.Crop,
		)
		Spacer(Modifier.width(8.dp))
		Text(
			text = friend.nickname,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium
		)
	}
}

@Composable
private fun FirstChar(
	first: String
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 32.dp,
				vertical = 8.dp
			)
	) {
		Text(
			text = first,
			color = MaterialTheme.colorScheme.outline,
			style = MaterialTheme.typography.bodyMedium
		)
	}
	HorizontalDivider(
		modifier = Modifier.padding(start = 32.dp),
		color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
	)
	Spacer(modifier = Modifier.height(8.dp))
}