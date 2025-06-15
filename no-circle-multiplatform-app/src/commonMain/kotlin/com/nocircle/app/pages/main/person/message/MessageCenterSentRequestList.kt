package com.nocircle.app.pages.main.person.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nocircle.app.api.RequestDTO
import com.nocircle.app.api.RequestDTO.RequestStatus
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.NoColor
import com.nocircle.common.expends.format
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoAsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageCenterSentRequestList() {
	val viewModel = koinViewModel<MessageCenterViewModel>()
	val requests by viewModel.sentRequests.collectAsState()
	
	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		itemsIndexed(
			items = requests,
			key = { _, it -> it.id }
		) { index, request ->
			Spacer(Modifier.height(16.dp))
			SentRequestCard(
				request = request
			)
			if (index == requests.lastIndex) {
				Spacer(Modifier.height(16.dp))
			}
		}
	}
}

@Composable
private fun SentRequestCard(
	request: RequestDTO
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.small)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.medium
			)
			.padding(12.dp)
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
			Column {
				Text(
					text = request.nickname ?: AppString.FRIENDS_ADD_FRIEND_NOT_NICKNAME.value(),
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.FRIENDS_ADD_FRIEND_ID.value().format(request.username),
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			Text(
				text = request.updateTime,
				modifier = Modifier.align(Alignment.BottomStart),
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			val color = request.status.getColor()
			Text(
				text = request.status.getString(),
				modifier = Modifier
					.align(Alignment.TopEnd)
					.clip(MaterialTheme.shapes.small)
					.border(
						width = 1.dp,
						color = color,
						shape = MaterialTheme.shapes.small
					)
					.background(
						color = color.copy(alpha = 0.1f),
						shape = MaterialTheme.shapes.small,
					)
					.padding(
						horizontal = 12.dp,
						vertical = 8.dp
					),
				textAlign = TextAlign.Center,
				style = MaterialTheme.typography.bodyMedium,
				color = color
			)
			if (request.status == RequestStatus.WAITING) {
				Text(
					text = "取消",
					modifier = Modifier
						.align(Alignment.BottomEnd)
						.clip(MaterialTheme.shapes.small)
						.background(
							color = MaterialTheme.colorScheme.surfaceContainerHighest,
							shape = MaterialTheme.shapes.small,
						)
						.clickable {
						
						}
						.padding(
							horizontal = 12.dp,
							vertical = 8.dp
						),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
			} else {
				Text(
					text = "删除",
					modifier = Modifier
						.align(Alignment.BottomEnd)
						.clip(MaterialTheme.shapes.small)
						.background(
							color = MaterialTheme.colorScheme.error,
							shape = MaterialTheme.shapes.small,
						)
						.clickable {
						
						}
						.padding(
							horizontal = 16.dp,
							vertical = 8.dp
						),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onError
				)
			}
		}
	}
}

@Composable
private fun RequestStatus.getColor(): Color {
	return when (this) {
		RequestStatus.AGREED -> NoColor.Green
		RequestStatus.REJECTED -> NoColor.Red
		RequestStatus.WAITING -> NoColor.Yellow
		RequestStatus.CANCELED -> NoColor.Gray
	}
}

@Composable
private fun RequestStatus.getString(): String {
	return when (this) {
		RequestStatus.AGREED -> AppString.MESSAGE_CENTER_AGREED
		RequestStatus.REJECTED -> AppString.MESSAGE_CENTER_REJECTED
		RequestStatus.WAITING -> AppString.MESSAGE_CENTER_WAITING
		RequestStatus.CANCELED -> AppString.MESSAGE_CENTER_CANCELED
	}.value()
}