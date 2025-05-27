package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Settings
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
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.person_account
import com.nocircle.app.generated.resources.person_settings_subtitle
import com.nocircle.app.generated.resources.settings
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.common.expends.rememberHexToColor
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.compose.complex.NoOption
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.resources.value
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val verticalScrollState = rememberScrollState()
	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.TopCenter
	) {
		Column(
			modifier = Modifier
				.widthIn(max = 840.dp)
				.fillMaxSize()
				.verticalScroll(verticalScrollState)
				.padding(horizontal = 16.dp, vertical = 32.dp)
		) {
			UserDetailCard()
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
			.padding(24.dp)
			.height(80.dp)
	) {
		val userDetail by viewModel.userDetail.collectAsState()
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
				text = Res.string.person_account.value(userDetail?.username ?: ""),
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
				val labels by viewModel.labels.collectAsState()
				labels?.let {
					it.fastForEachIndexed { index, label ->
						Label(
							label = label.label,
							color = label.color,
						)
						Spacer(modifier = Modifier.width(6.dp))
					}
					EditLabel(
						icon = if (it.size < 5) Icons.Rounded.Add else Icons.Rounded.Remove
					)
				}
			}
		}
	}
}

@Composable
private fun Label(
	label: String,
	color: String,
) {
	val containerColor = rememberHexToColor(color)
	Box(
		modifier = Modifier
			.fillMaxHeight()
			.background(
				color = containerColor,
				shape = MaterialTheme.shapes.extraSmall
			)
			.padding(horizontal = 6.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = label,
			color = if (containerColor.luminance() > 0.5f) Color.Black else Color.White,
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
			.size(22.dp),
		tint = MaterialTheme.colorScheme.outline,
		shape = MaterialTheme.shapes.extraSmall,
		paddingValues = PaddingValues()
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
private fun OptionList() {
	val controller = LocalNavController.current
	NoOption(
		title = Res.string.settings.value(),
		subtitle = Res.string.person_settings_subtitle.value(),
		icon = Icons.Rounded.Settings,
	) {
		controller.navigate(route = SettingsRoute)
	}
}