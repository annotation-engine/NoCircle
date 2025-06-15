package com.nocircle.compose.material3

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.zIndex
import com.nocircle.common.expends.noLocalProvidedFor

@Composable
fun NoTabRow(
	selectedTabIndex: Int,
	modifier: Modifier = Modifier,
	containerColor: Color = TabRowDefaults.primaryContainerColor,
	contentColor: Color = TabRowDefaults.primaryContainerColor,
	shape: Shape = MaterialTheme.shapes.medium,
	tabs: @Composable () -> Unit
) {
	CompositionLocalProvider(
		LocalNoTabShape provides shape
	) {
		TabRow(
			selectedTabIndex = selectedTabIndex,
			modifier = modifier,
			containerColor = containerColor,
			contentColor = contentColor,
			indicator = { tabPositions ->
				if (selectedTabIndex < tabPositions.size) {
					Box(
						modifier = Modifier
							.tabIndicatorOffset(tabPositions[selectedTabIndex])
							.fillMaxSize()
							.clip(LocalNoTabShape.current)
							.background(
								color = MaterialTheme.colorScheme.primary,
								shape = LocalNoTabShape.current
							)
							.zIndex(-1f)
					)
				}
			},
			divider = {},
			tabs = tabs
		)
	}
}

@Composable
fun NoTab(
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
	unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
	content: @Composable RowScope.() -> Unit
) {
	Tab(
		selected = selected,
		onClick = onClick,
		modifier = modifier.clip(LocalNoTabShape.current),
		enabled = enabled,
		text = {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				val contentColor by animateColorAsState(
					targetValue = if (selected) selectedContentColor else unselectedContentColor
				)
				CompositionLocalProvider(
					LocalContentColor provides contentColor
				) {
					content()
				}
			}
		},
		selectedContentColor = MaterialTheme.colorScheme.onSurface,
		unselectedContentColor = MaterialTheme.colorScheme.onSurface
	)
}

private val LocalNoTabShape = compositionLocalOf<Shape> {
	noLocalProvidedFor("LocalNoTabShape")
}