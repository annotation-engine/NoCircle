package com.nocircle.app.pages.friends.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.friends.list.FriendListViewModel.SortOrder.ASC
import com.nocircle.app.pages.friends.list.FriendListViewModel.SortOrder.DESC
import com.nocircle.app.pages.friends.list.add.AddFriendSheet
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.constants.StringConstants
import com.nocircle.common.device.DeviceType
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import com.nocircle.shared.model.friend.FriendDTO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendList(
	current: Int?,
	navigate: (Int) -> Unit
) {
	val viewModel = koinViewModel<FriendListViewModel>()
	NoScaffold(
		topBar = {
			val isCompact = WindowWidthSizes.isCompact
			NoTopAppBar(
				actions = {
					FriendSearch(
						viewModel = viewModel,
						isCompact = isCompact,
					)
				},
				contentPadding = if (isCompact) NoTopAppBarDefaults.MediumContentPadding else NoTopAppBarDefaults.SmallContentPadding
			)
		}
	) { paddingValues ->
		val showFriendSearchList by viewModel.showFriendSearchList.collectAsState()
		if (showFriendSearchList) {
			FriendSearchList(
				viewModel = viewModel,
				current = current,
				navigate = navigate,
				modifier = Modifier
					.fillMaxSize()
					.padding(paddingValues)
			)
		} else {
			val isCompact = WindowWidthSizes.isCompact
			FriendList(
				viewModel = viewModel,
				isCompact = isCompact,
				current = current,
				navigate = navigate,
				modifier = Modifier
					.fillMaxSize()
					.padding(paddingValues)
			)
		}
	}
}

@Composable
private fun FriendSearch(
	viewModel: FriendListViewModel,
	isCompact: Boolean,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		val search by viewModel.search.collectAsState()
		val size by remember(isCompact) {
			derivedStateOf {
				when {
					isCompact -> 48.dp
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
					text = AppString.FRIEND_SEARCH.value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
		Spacer(modifier = Modifier.width(if (isCompact) 12.dp else 8.dp))
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
	viewModel: FriendListViewModel,
	isCompact: Boolean,
	current: Int?,
	navigate: (Int) -> Unit,
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
				top = 16.dp
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
				FriendItem(
					item = it,
					selected = current == it.friendId,
					navigate = navigate,
				)
			}
		}
		var isShowSortOrder by remember { mutableStateOf(false) }
		val alpha by animateFloatAsState(
			targetValue = if (isShowSortOrder) 1f else 0f
		)
		LaunchedEffect(scrollState) {
			snapshotFlow {
				val lastVisible = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()
				val lastVisibleIndex = scrollState.layoutInfo.totalItemsCount - 1
				lastVisible?.index == lastVisibleIndex
			}.distinctUntilChanged().collectLatest {
				isShowSortOrder = !it
			}
		}
		SwitchSortOrder(
			viewModel = viewModel,
			isCompact = isCompact,
			alpha = alpha,
		)
	}
}

@Composable
private fun FriendItem(
	item: FriendDTO,
	selected: Boolean,
	navigate: (Int) -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
			)
			.clickable {
				navigate(item.friendId)
			}
			.padding(
				start = 32.dp,
				top = 16.dp,
				end = 16.dp,
				bottom = 16.dp
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
		Spacer(Modifier.width(12.dp))
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
		color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
	)
}

@Composable
private fun BoxScope.SwitchSortOrder(
	viewModel: FriendListViewModel,
	isCompact: Boolean,
	alpha: Float
) {
	val sortOrder by viewModel.sortOrder.collectAsState()
	val switchSortOrder = {
		viewModel.sortOrder.value = if (sortOrder == ASC) DESC else ASC
	}
	val rotate by animateFloatAsState(
		targetValue = if (sortOrder == DESC) 0f else 180f
	)
	val show by remember(alpha) {
		derivedStateOf { alpha > 0f }
	}
	if (!show) return
	if (isCompact) {
		FloatingActionButton(
			onClick = switchSortOrder,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.offset(x = (-16).dp, y = (-16).dp)
				.alpha(alpha)
		) {
			NoIcon(
				icon = AppIcon.TextRotateUp.value(),
				modifier = Modifier.rotate(rotate)
			)
		}
	} else {
		SmallFloatingActionButton(
			onClick = switchSortOrder,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.offset(x = (-12).dp, y = (-12).dp)
				.alpha(alpha)
		) {
			NoIcon(
				icon = AppIcon.TextRotateUp.value(),
				modifier = Modifier.rotate(rotate)
			)
		}
	}
}

@Composable
private fun FriendSearchList(
	viewModel: FriendListViewModel,
	current: Int?,
	navigate: (Int) -> Unit,
	modifier: Modifier = Modifier,
) {
	val friendSearchList by viewModel.friendSearchList.collectAsState()
	val scrollState = rememberLazyListState()
	LaunchedEffect(friendSearchList) {
		scrollState.scrollToItem(0)
	}
	LazyColumn(
		modifier = modifier,
		state = scrollState
	) {
		item {
			FriendSearchHint(friendSearchList.size)
		}
		itemsIndexed(
			items = friendSearchList
		) { index, it ->
			FriendSearchItem(
				item = it,
				selected = current == it.friendId,
				navigate = navigate
			)
		}
	}
}

@Composable
private fun FriendSearchHint(
	count: Int
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				vertical = 16.dp
			),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = when (count) {
				0 -> AppString.FRIEND_SEARCH_NOT_FOUND.value()
				1 -> AppString.FRIEND_SEARCH_FOUND_ONE.value()
				else -> AppString.FRIEND_SEARCH_FOUND_MORE.value(count)
			},
			color = MaterialTheme.colorScheme.outline,
			style = MaterialTheme.typography.bodyMedium
		)
	}
	HorizontalDivider(
		color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
	)
}

@Composable
private fun FriendSearchItem(
	item: FriendListViewModel.FriendSearch,
	selected: Boolean,
	navigate: (Int) -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
			)
			.clickable {
				navigate(item.friendId)
			}
			.padding(
				start = 32.dp,
				top = 16.dp,
				end = 16.dp,
				bottom = 16.dp
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
			val contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
			Text(
				text = getAnnotatedString(item.nickname, item.nicknameIndices),
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				color = contentColor,
				style = MaterialTheme.typography.titleMedium
			)
			Text(
				text = AnnotatedID + getAnnotatedString(item.username, item.usernameIndices),
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				color = contentColor,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

private val AnnotatedID = buildAnnotatedString {
	append(StringConstants.ID)
}

@Composable
private fun getAnnotatedString(
	text: String,
	indices: List<IntRange>
) = buildAnnotatedString {
	val spanStyle = SpanStyle(
		color = MaterialTheme.colorScheme.primary
	)
	text.forEachIndexed { index, char ->
		if (indices.any { index in it }) {
			withStyle(spanStyle) {
				append(char)
			}
		} else {
			append(char)
		}
	}
}