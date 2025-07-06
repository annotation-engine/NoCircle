package com.nocircle.app.pages.person.message

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.NoColor
import com.nocircle.common.constants.StringConstants
import com.nocircle.compose.expends.format
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.*
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.model.friend.request.FriendRequestDTO.Status.*
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun MessageCenterSentRequestList() {
	val viewModel = koinViewModel<MessageCenterViewModel>()
	val requests by viewModel.sentRequests.collectAsState()
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
				SentRequestCard(
					viewModel = viewModel,
					item = it
				)
			}
		}
	} else {
		NoMessage()
	}
}

@OptIn(ExperimentalTime::class)
@Composable
private fun SentRequestCard(
	viewModel: MessageCenterViewModel,
	item: FriendRequestDTO
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.height(154.dp)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(90.dp)
		) {
			NoAsyncImage(
				url = item.avatarUrl,
				modifier = Modifier
					.size(90.dp)
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
						text = item.nickname,
						color = MaterialTheme.colorScheme.onSurface,
						style = MaterialTheme.typography.titleMedium,
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = StringConstants.ID + item.username,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						style = MaterialTheme.typography.bodyMedium,
					)
				}
				Text(
					text = item.createTime.format(),
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
				NoTag(
					text = item.status.getString(),
					modifier = Modifier.align(Alignment.TopEnd),
					color = item.status.getColor(),
					style = MaterialTheme.typography.bodyMedium,
					type = NoTagType.Border()
				)
			}
		}
		Row(
			modifier = Modifier.fillMaxSize(),
			verticalAlignment = Alignment.Bottom
		) {
			val showLabels by remember(item.labels.size) {
				derivedStateOf { item.labels.isNotEmpty() }
			}
			if (showLabels) {
				item.labels.fastForEach {
					NoTag(
						text = it.label,
						color = hexToColor(it.color)
					)
					Spacer(modifier = Modifier.width(6.dp))
				}
			} else {
				NoTag(
					text = AppString.PERSON_MESSAGE_NO_LABEL.value(),
					color = MaterialTheme.colorScheme.surfaceContainerHighest
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			if (item.status == PENDING) {
				NoButton(
					text = AppString.MESSAGE_CENTER_CANCEL.value(),
					modifier = Modifier.height(36.dp),
					style = MaterialTheme.typography.bodyMedium,
					colors = NoButtonColors.ErrorColors,
					contentPadding = NoButtonDefaults.TextButtonContentPadding
				) {
					viewModel.cancelSentRequest(item.id, item.targetId)
				}
			} else {
				NoButton(
					text = AppString.MESSAGE_CENTER_DELETE.value(),
					modifier = Modifier.height(36.dp),
					style = MaterialTheme.typography.bodyMedium,
					colors = NoButtonColors.SurfaceContainerHighestColors,
					contentPadding = NoButtonDefaults.TextButtonContentPadding
				) {
					viewModel.deleteSentRequest(item.targetId)
				}
			}
		}
	}
}

@Composable
private fun FriendRequestDTO.Status.getColor(): Color {
	return when (this) {
		AGREED -> NoColor.Green
		REJECTED -> NoColor.Red
		PENDING -> NoColor.Yellow
		CANCELED -> NoColor.Gray
	}
}

@Composable
private fun FriendRequestDTO.Status.getString(): String {
	return when (this) {
		AGREED -> AppString.MESSAGE_CENTER_AGREED
		REJECTED -> AppString.MESSAGE_CENTER_REJECTED
		PENDING -> AppString.MESSAGE_CENTER_PENDING
		CANCELED -> AppString.MESSAGE_CENTER_CANCELED
	}.value()
}