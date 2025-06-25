package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.compose.desktop.NoWindowDraggableArea
import com.nocircle.compose.windowsize.WindowWidthSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTopAppBar(
	modifier: Modifier = Modifier,
	title: (@Composable RowScope.() -> Unit)? = null,
	navigationIcon: (@Composable () -> Unit)? = null,
	actions: (@Composable RowScope.() -> Unit)? = null,
	contentPadding: PaddingValues = NoTopAppBarDefaults.contentPadding,
	colors: NoTopAppBarColors = NoTopAppBarDefaults.topAppBarColors
) {
	val paddingValues = NoTopAppBarDefaults.windowInsets.asPaddingValues()
	val isCompact = WindowWidthSizes.isCompact
	val deviceType = NoDevice.Type
	val windowInsets by remember(paddingValues, isCompact) {
		derivedStateOf {
			WindowInsets(
				top = when {
					!isCompact -> paddingValues.calculateTopPadding() / 2
					deviceType == DeviceType.DESKTOP -> paddingValues.calculateTopPadding() + 20.dp
					else -> paddingValues.calculateTopPadding()
				}
			)
		}
	}
	NoWindowDraggableArea {
		Row(
			modifier = modifier
				.shadow(
					elevation = 4.dp,
					ambientColor = colors.shadowColor,
					spotColor = colors.shadowColor,
				)
				.fillMaxWidth()
				.background(colors.containerColor)
				.windowInsetsPadding(windowInsets)
				.padding(contentPadding),
			verticalAlignment = Alignment.CenterVertically,
		) {
			if (navigationIcon != null) {
				CompositionLocalProvider(
					LocalContentColor provides colors.navigationIconContentColor
				) {
					navigationIcon()
				}
			}
			if (title != null) {
				Spacer(Modifier.width(8.dp))
				CompositionLocalProvider(
					LocalContentColor provides colors.titleContentColor,
					LocalTextStyle provides MaterialTheme.typography.titleLarge
				) {
					title()
				}
				Spacer(Modifier.width(8.dp))
			}
			Spacer(Modifier.weight(1f))
			if (actions != null) {
				CompositionLocalProvider(
					LocalContentColor provides colors.actionIconContentColor
				) {
					actions()
				}
			}
		}
	}
}

@Immutable
object NoTopAppBarDefaults {
	
	@OptIn(ExperimentalMaterial3Api::class)
	val windowInsets: WindowInsets
		@Composable
		get() = TopAppBarDefaults.windowInsets
	
	val topAppBarColors: NoTopAppBarColors
		@Composable
		get() = NoTopAppBarColors(
			containerColor = MaterialTheme.colorScheme.surface,
			navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
			titleContentColor = MaterialTheme.colorScheme.onSurface,
			actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
			shadowColor = MaterialTheme.colorScheme.onSurface
		)
	
	val contentPadding = PaddingValues(16.dp)
}

@Immutable
data class NoTopAppBarColors(
	val containerColor: Color,
	val navigationIconContentColor: Color,
	val titleContentColor: Color,
	val actionIconContentColor: Color,
	val shadowColor: Color
)