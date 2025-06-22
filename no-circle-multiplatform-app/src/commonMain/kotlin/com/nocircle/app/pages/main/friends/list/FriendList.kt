package com.nocircle.app.pages.main.friends.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TextRotateUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.main.friends.list.FriendsListViewModel.SortOrder.ASC
import com.nocircle.app.pages.main.friends.list.FriendsListViewModel.SortOrder.DESC
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
					FriendSearch(
						viewModel = viewModel,
						isCompat = isCompat,
					)
				},
				contentPadding = if (isCompat) NoTopAppBarDefaults.contentPadding else MediumContentPadding
			)
		}
	) { paddingValues ->
		val showFriendSearchList by viewModel.showFriendSearchList.collectAsState()
		if (showFriendSearchList) {
			FriendSearchList(
				viewModel = viewModel,
				modifier = Modifier
					.fillMaxSize()
					.padding(paddingValues)
			)
		} else {
			FriendList(
				viewModel = viewModel,
				isCompat = isCompat,
				modifier = Modifier
					.fillMaxSize()
					.padding(paddingValues)
			)
		}
	}
}

private val MediumContentPadding = PaddingValues(12.dp)

@Composable
private fun FriendSearch(
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
					isCompat -> 48.dp
					DeviceType.isDesktop -> 40.dp
					else -> 44.dp
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
private fun FriendList(
	viewModel: FriendsListViewModel,
	isCompat: Boolean,
	modifier: Modifier = Modifier,
) {
	val friendList by viewModel.friendList.collectAsState()
	Box(
		modifier = modifier
	) {
		val scrollState = rememberLazyListState()
		LaunchedEffect(friendList) {
			scrollState.scrollToItem(0)
		}
		LazyColumn(
			modifier = Modifier
				.fillMaxSize(),
			state = scrollState,
			contentPadding = PaddingValues(
				top = 10.dp,
				bottom = 10.dp
			)
		) {
			itemsIndexed(
				items = friendList
			) { index, it ->
				val first = it.pinyin.first().uppercase()
				val showSubtitle = index == 0 || friendList[index - 1].pinyin.first().uppercase() != first
				if (showSubtitle) {
					FriendSubtitleItem(first)
				}
				FriendItem(it)
			}
		}
		SwitchSortOrder(
			viewModel = viewModel,
			isCompat = isCompat
		)
	}
}

@Composable
private fun FriendItem(
	item: FriendDTO,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				start = 32.dp,
				top = 10.dp,
				end = 16.dp,
				bottom = 10.dp
			)
			.height(44.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		NoAsyncImage(
			url = item.avatarUrl,
			modifier = Modifier
				.size(44.dp)
				.clip(MaterialTheme.shapes.medium),
			contentScale = ContentScale.Crop,
		)
		Spacer(Modifier.width(8.dp))
		Text(
			text = item.nickname,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium
		)
	}
}

@Composable
private fun FriendSubtitleItem(
	first: String
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 32.dp,
				vertical = 10.dp
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
	Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun BoxScope.SwitchSortOrder(
	viewModel: FriendsListViewModel,
	isCompat: Boolean,
) {
	val sortOrder by viewModel.sortOrder.collectAsState()
	val switchSortOrder = {
		viewModel.sortOrder.value = if (sortOrder == ASC) DESC else ASC
	}
	val rotate by animateFloatAsState(
		targetValue = if (sortOrder == DESC) 0f else 180f
	)
	if (isCompat) {
		FloatingActionButton(
			onClick = switchSortOrder,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.offset(x = (-16).dp, y = (-16).dp)
		) {
			NoIcon(
				icon = Icons.Outlined.TextRotateUp,
				modifier = Modifier.rotate(rotate)
			)
		}
	} else {
		SmallFloatingActionButton(
			onClick = switchSortOrder,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.offset(x = (-12).dp, y = (-12).dp)
		) {
			NoIcon(
				icon = Icons.Outlined.TextRotateUp,
				modifier = Modifier.rotate(rotate)
			)
		}
	}
}

@Composable
private fun FriendSearchList(
	viewModel: FriendsListViewModel,
	modifier: Modifier = Modifier,
) {
	val friendSearchList by viewModel.friendSearchList.collectAsState()
	val scrollState = rememberLazyListState()
	LaunchedEffect(friendSearchList) {
		scrollState.scrollToItem(0)
	}
	LazyColumn(
		modifier = modifier,
		state = scrollState,
		contentPadding = PaddingValues(
			top = 10.dp,
			bottom = 10.dp
		)
	) {
		itemsIndexed(
			items = friendSearchList
		) { index, it ->
			FriendSearchItem(it)
		}
	}
}

@Composable
private fun FriendSearchItem(
	item: FriendsListViewModel.FriendSearch
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				start = 32.dp,
				top = 10.dp,
				end = 16.dp,
				bottom = 10.dp
			)
			.height(44.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		NoAsyncImage(
			url = item.avatarUrl,
			modifier = Modifier
				.size(44.dp)
				.clip(MaterialTheme.shapes.medium),
			contentScale = ContentScale.Crop,
		)
		Spacer(Modifier.width(8.dp))
		Column(
			modifier = Modifier
				.fillMaxSize(),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = item.nickname,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.titleMedium
			)
			Text(
				text = "ID: ${item.username}",
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}