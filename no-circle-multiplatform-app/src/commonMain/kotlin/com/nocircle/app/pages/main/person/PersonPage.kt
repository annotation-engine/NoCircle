package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.format
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.resources.value
import com.nocircle.compose.complex.NoOption
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val verticalScrollState = rememberScrollState()
	NoScaffold { paddingValues ->
		Column(
			modifier = Modifier
				.widthIn(max = 840.dp)
				.fillMaxSize()
				.verticalScroll(verticalScrollState)
				.padding(paddingValues)
				.padding(16.dp)
		) {
			UserDetailCard()
			Spacer(modifier = Modifier.height(16.dp))
			LastLoginTime()
			Spacer(modifier = Modifier.height(16.dp))
			OptionList()
		}
	}
}

@Composable
private fun UserDetailCard() {
	val viewModel = koinViewModel<PersonViewModel>()
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.small
			)
			.padding(16.dp)
			.height(100.dp)
	) {
		val userDetail by viewModel.userDetail.collectAsState()
		NoAsyncImage(
			url = userDetail?.avatarUrl,
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
					text = userDetail?.nickname ?: "",
					color = MaterialTheme.colorScheme.onSurface,
					style = MaterialTheme.typography.titleMedium,
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = AppString.PersonAccount.value().format(userDetail?.username ?: ""),
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
					.background(
						color = if (userDetail == null) MaterialTheme.colorScheme.surface else Color.Transparent,
						shape = MaterialTheme.shapes.extraSmall
					)
			) {
				val labels by viewModel.labels.collectAsState()
				labels.let {
					it.fastForEachIndexed { index, label ->
						Label(
							label = label.label,
							color = label.color,
						)
						Spacer(modifier = Modifier.width(6.dp))
					}
					EditLabel(
						icon = if (it.size < 4) AppIcon.Add.value else AppIcon.Remove.value
					)
				}
			}
		}
	}
}

@Composable
private fun Label(
	label: String,
	color: Int,
) {
	val color by remember(color) {
		derivedStateOf { Color(color) }
	}
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

@Composable
private fun EditLabel(
	icon: ImageVector
) {
	var showModal by remember { mutableStateOf(false) }
	NoIconButton(
		icon = icon,
		modifier = Modifier
			.size(24.dp),
		tint = MaterialTheme.colorScheme.outline,
		shape = MaterialTheme.shapes.extraSmall,
		contentPadding = PaddingValues()
	) {
		showModal = true
	}
	if (showModal) {
		EditLabelSheet(
			onDismissRequest = { showModal = false }
		)
	}
}

@Composable
private fun LastLoginTime() {
	val viewModel = koinViewModel<PersonViewModel>()
	val userDetail by viewModel.userDetail.collectAsState()
	val lastLoginTime = userDetail?.lastLoginTime
	if (lastLoginTime != null) {
		NoOption(
			title = AppString.PersonLastLoginTime.value(),
			subtitle = lastLoginTime,
			icon = AppIcon.AccessTime.value
		)
	}
}

@Composable
private fun OptionList() {
	val controller = LocalNavController.current
	NoOption(
		title = AppString.Settings.value(),
		subtitle = AppString.PersonSettingsSubtitle.value(),
		icon = AppIcon.Settings.value
	) {
		controller.navigate(route = SettingsRoute)
	}
}