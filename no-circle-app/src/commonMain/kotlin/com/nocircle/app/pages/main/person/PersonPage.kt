package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.twotone.Message
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.twotone.Group
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.LocalNavController
import com.nocircle.app.NoRoutes
import com.nocircle.app.generated.resources.*
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.expends.noNavigate
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val verticalScrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.widthIn(max = 700.dp)
			.fillMaxSize()
			.verticalScroll(verticalScrollState)
			.padding(horizontal = 16.dp, vertical = 32.dp)
	) {
		UserDetailCard()
		Spacer(modifier = Modifier.height(16.dp))
		UserInformationCard()
		Spacer(modifier = Modifier.height(16.dp))
		OptionList()
	}
}

@Composable
private fun UserDetailCard() {
	val viewModel = koinViewModel<PersonViewModel>()
	val userDetail by viewModel.userDetail.collectAsState()
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.small
			)
			.padding(24.dp)
			.height(80.dp)
	) {
		NoAsyncImage(
			url = userDetail?.avatarUrl,
			modifier = Modifier
				.size(80.dp)
				.clip(MaterialTheme.shapes.small),
			placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceDim),
			contentScale = ContentScale.Crop
		)
		Spacer(modifier = Modifier.width(12.dp))
		Column(
			modifier = Modifier
				.fillMaxHeight()
				.padding(vertical = 2.dp),
		) {
			Text(
				text = userDetail?.nickname ?: "",
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "${Res.string.person_account.value}${userDetail?.username}",
				color = MaterialTheme.colorScheme.outline,
				style = MaterialTheme.typography.bodyMedium,
			)
			Spacer(modifier = Modifier.weight(1f))
			Row(
				modifier = Modifier
					.height(22.dp)
					.background(
						color = if (userDetail == null) MaterialTheme.colorScheme.surface else Color.Transparent,
						shape = MaterialTheme.shapes.extraSmall
					)
			) {
				if (userDetail == null) {
					Box(
						modifier = Modifier
							.fillMaxHeight()
							.width(80.dp)
							.background(
								color = MaterialTheme.colorScheme.surface,
								shape = MaterialTheme.shapes.extraSmall
							)
					)
				} else {
					val labels = userDetail!!.labels.toList()
					if (labels.isEmpty()) {
						// 添加标签
					} else {
						labels.fastForEachIndexed { index, (label, color) ->
							Box(
								modifier = Modifier
									.fillMaxHeight()
									.background(
										color = remember(color) { color.hexToColor() },
										shape = MaterialTheme.shapes.extraSmall
									)
									.padding(horizontal = 6.dp),
								contentAlignment = Alignment.Center
							) {
								Text(
									text = label,
									color = Color.White,
									style = MaterialTheme.typography.labelMedium
								)
							}
							if (index < labels.size - 1) {
								Spacer(modifier = Modifier.width(6.dp))
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun UserInformationCard() {
	val viewModel = koinViewModel<PersonViewModel>()
	val userInformation by viewModel.userInformation.collectAsState()
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.height(56.dp)
	) {
		Row(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = MaterialTheme.shapes.small
				)
				.padding(
					horizontal = 12.dp
				),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = Icons.TwoTone.Person,
				modifier = Modifier
					.size(24.dp),
				tint = MaterialTheme.colorScheme.onPrimaryContainer,
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = Res.string.person_friend_count.value,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.bodyMedium,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "${userInformation?.friendCount ?: 0}",
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(modifier = Modifier.width(16.dp))
		Row(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(
					color = MaterialTheme.colorScheme.secondaryContainer,
					shape = MaterialTheme.shapes.small
				)
				.padding(
					horizontal = 12.dp
				),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = Icons.TwoTone.Group,
				modifier = Modifier
					.size(24.dp),
				tint = MaterialTheme.colorScheme.onSecondaryContainer,
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = Res.string.person_group_count.value,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.bodyMedium,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "${userInformation?.groupCount ?: 0}",
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(modifier = Modifier.width(16.dp))
		Row(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(
					color = MaterialTheme.colorScheme.tertiaryContainer,
					shape = MaterialTheme.shapes.small
				)
				.padding(
					horizontal = 12.dp
				),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = Icons.AutoMirrored.TwoTone.Message,
				modifier = Modifier
					.size(24.dp),
				tint = MaterialTheme.colorScheme.onTertiaryContainer,
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = Res.string.person_message_count.value,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.bodyMedium,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "${userInformation?.messageCount ?: 0}",
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				style = MaterialTheme.typography.titleLarge
			)
		}
	}
}

@Composable
private fun OptionList() {
	val navController = LocalNavController.current
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.height(56.dp)
			.clip(MaterialTheme.shapes.small)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.small
			)
			.clickable {
				navController.noNavigate(NoRoutes.Settings)
			}
			.padding(horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		NoIcon(
			icon = Icons.Rounded.Settings,
			tint = MaterialTheme.colorScheme.onTertiaryContainer
		)
		Spacer(modifier = Modifier.width(8.dp))
		Text(
			text = Res.string.settings.value,
			color = MaterialTheme.colorScheme.onTertiaryContainer,
			style = MaterialTheme.typography.titleMedium,
		)
		Spacer(modifier = Modifier.weight(1f))
		NoIcon(
			icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
			tint = MaterialTheme.colorScheme.onTertiaryContainer
		)
	}
}