package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nocircle.compose.foundation.LocalNoIconTintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTopAppBar(
	title: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	navigationIcon: @Composable (() -> Unit)? = null,
	actions: @Composable (RowScope.() -> Unit)? = null,
	windowInsets: WindowInsets = NoTopAppBarDefaults.windowInsets,
	colors: NoTopAppBarColors = NoTopAppBarDefaults.topAppBarColors
) {
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
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		if (navigationIcon != null) {
			CompositionLocalProvider(
				LocalNoIconTintColor provides colors.navigationIconContentColor,
				content = navigationIcon
			)
			Spacer(Modifier.width(8.dp))
		}
		CompositionLocalProvider(
			LocalContentColor provides colors.titleContentColor,
			LocalTextStyle provides MaterialTheme.typography.titleLarge,
			content = title
		)
		Spacer(Modifier.weight(1f))
		if (actions != null) {
			CompositionLocalProvider(
				LocalNoIconTintColor provides colors.actionIconContentColor
			) {
				actions()
			}
		}
	}
}

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
}

data class NoTopAppBarColors(
	val containerColor: Color,
	val navigationIconContentColor: Color,
	val titleContentColor: Color,
	val actionIconContentColor: Color,
	val shadowColor: Color
)