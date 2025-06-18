package com.nocircle.app.pages.main.person.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppString
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.*
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageCenterReceivedRequestList() {
	val viewModel = koinViewModel<MessageCenterViewModel>()
	val requests by viewModel.receivedRequests.collectAsState()
	
	val showList by remember(requests.size) {
		derivedStateOf { requests.isNotEmpty() }
	}
	if (showList) {
		val isExpended = WindowWidthSizes.isExpended
		val columns by remember(isExpended) {
			derivedStateOf { GridCells.Fixed(if (isExpended) 2 else 1) }
		}
		LazyVerticalGrid(
			columns = columns,
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			items(requests) {
				ReceivedRequestCard(
					viewModel = viewModel,
					request = it
				)
			}
		}
	} else {
		NoMessage()
	}
}

@Composable
private fun ReceivedRequestCard(
	viewModel: MessageCenterViewModel,
	request: FriendRequestDTO
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.height(172.dp)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(100.dp)
		) {
			NoAsyncImage(
				url = request.avatarUrl,
				modifier = Modifier
					.size(100.dp)
					.clip(MaterialTheme.shapes.small),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.width(12.dp))
			Box(
				modifier = Modifier
					.fillMaxSize()
			) {
				Column(
					modifier = Modifier
						.align(Alignment.TopStart)
				) {
					Text(
						text = request.nickname ?: AppString.FRIENDS_ADD_FRIEND_NOT_NICKNAME.value(),
						color = MaterialTheme.colorScheme.onSurface,
						style = MaterialTheme.typography.titleMedium,
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = AppString.FRIENDS_ADD_FRIEND_ID.value(request.username),
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						style = MaterialTheme.typography.bodyMedium,
					)
				}
				Text(
					text = request.createTime,
					modifier = Modifier
						.align(Alignment.BottomStart)
						.clip(MaterialTheme.shapes.extraSmall)
						.background(
							color = MaterialTheme.colorScheme.surfaceContainerHighest,
							shape = MaterialTheme.shapes.extraSmall
						)
						.padding(
							horizontal = 6.dp,
							vertical = 3.dp
						),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
		Row(
			modifier = Modifier.fillMaxSize(),
			verticalAlignment = Alignment.Bottom
		) {
			val showLabels by remember(request.labels.size) {
				derivedStateOf { request.labels.isNotEmpty() }
			}
			if (showLabels) {
				request.labels.fastForEach {
					NoTag(
						text = it.label,
						color = hexToColor(it.color),
						contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
					)
					Spacer(modifier = Modifier.width(6.dp))
				}
			} else {
				NoTag(
					text = AppString.PERSON_MESSAGE_NO_LABEL.value(),
					color = MaterialTheme.colorScheme.surfaceContainerHighest,
					contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			NoButton(
				text = AppString.MESSAGE_CENTER_AGREE.value(),
				modifier = Modifier.height(36.dp),
				style = MaterialTheme.typography.bodyMedium,
				colors = NoButtonColors.PrimaryColors,
				contentPadding = NoButtonDefaults.TextButtonContentPadding
			) {
				viewModel.agreeReceivedRequest(request.id, request.targetId)
			}
			Spacer(modifier = Modifier.width(8.dp))
			NoButton(
				text = AppString.MESSAGE_CENTER_REJECT.value(),
				modifier = Modifier.height(36.dp),
				style = MaterialTheme.typography.bodyMedium,
				colors = NoButtonColors.ErrorColors,
				contentPadding = NoButtonDefaults.TextButtonContentPadding
			) {
				viewModel.rejectReceivedRequest(request.id, request.targetId)
			}
		}
	}
}