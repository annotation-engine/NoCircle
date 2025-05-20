package com.nocircle.app.pages.main.person

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoNavHost
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.person_account
import com.nocircle.app.generated.resources.settings
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.expends.value
import com.nocircle.common.navigation.NoNavHost
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.compose.NoOption
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoModalBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPageAdapter() {
	if (WindowWidthSizes.isCompact) {
		PersonPage()
	} else {
		PersonPageNavHost()
	}
}

@Composable
private fun PersonPageNavHost() {
	val navController = NoNavControllerManagers.get(NoNavHost.Person)
	NoNavHost(
		navController = navController,
		startDestination = MainRoute,
		enterTransition = { EnterTransition },
		exitTransition = { ExitTransition },
		popEnterTransition = { EnterTransition },
		popExitTransition = { ExitTransition },
	) {
		composable<MainRoute> { PersonPage() }
		composable<SettingsRoute> { SettingsPage() }
		composable<AppearanceRoute> { AppearancePage() }
	}
}

private val EnterTransition = fadeIn(animationSpec = tween(120))
private val ExitTransition = fadeOut(animationSpec = tween(120))

@Composable
private fun PersonPage() {
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
				text = "${Res.string.person_account.value()}${userDetail?.username}",
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
				if (labels != null) {
					labels!!.fastForEachIndexed { index, label ->
						Label(
							label = label.label,
							color = label.color,
						)
						Spacer(modifier = Modifier.width(6.dp))
					}
					if (labels!!.size < 5) {
						EditLabel()
					}
				}
			}
		}
	}
}

@Composable
private fun Label(
	label: String,
	color: Any,
) {
	Box(
		modifier = Modifier
			.fillMaxHeight()
			.background(
				color = when (color) {
					is String -> remember(color) { color.hexToColor() }
					is Color -> color
					else -> error("不支持的 color 类型")
				},
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
}

@Composable
private fun EditLabel() {
	var showModal by remember { mutableStateOf(false) }
	NoIconButton(
		icon = Icons.Rounded.Add,
		modifier = Modifier
			.size(22.dp),
		tint = MaterialTheme.colorScheme.outline,
		shape = MaterialTheme.shapes.extraSmall,
		paddingValues = PaddingValues()
	) {
		showModal = true
	}
	if (showModal) {
		NoModalBottomSheet(
			onDismissRequest = { showModal = false },
		) {
			Spacer(modifier = Modifier.height(200.dp))
		}
	}
}

@Composable
private fun OptionList() {
	val navController = NoNavControllerManagers.get(NoNavHost.Person)
	NoOption(
		title = Res.string.settings.value(),
		icon = Icons.Rounded.Settings,
	) {
		navController.navigate(SettingsRoute)
	}
}