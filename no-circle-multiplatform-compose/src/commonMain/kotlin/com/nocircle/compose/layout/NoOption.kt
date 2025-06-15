package com.nocircle.compose.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.resources.ComposeIcon

@Composable
fun NoOption(
	title: @Composable () -> Unit,
	icon: @Composable () -> Unit,
	actions: @Composable RowScope.() -> Unit,
	modifier: Modifier = Modifier,
	showSuffixIcon: Boolean = true,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
	shape: Shape = MaterialTheme.shapes.medium,
	onClick: (() -> Unit)? = null,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.height(60.dp)
			.clip(shape)
			.background(
				color = containerColor,
				shape = shape
			)
			.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
			.padding(horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		CompositionLocalProvider(
			LocalContentColor provides MaterialTheme.colorScheme.onSurface
		) {
			icon()
		}
		Spacer(modifier = Modifier.width(8.dp))
		CompositionLocalProvider(
			LocalContentColor provides MaterialTheme.colorScheme.onSurface,
			LocalTextStyle provides MaterialTheme.typography.titleMedium
		) {
			title()
		}
		Spacer(modifier = Modifier.width(16.dp))
		Row(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.End
		) {
			CompositionLocalProvider(
				LocalContentColor provides MaterialTheme.colorScheme.outline,
				LocalTextStyle provides MaterialTheme.typography.bodyMedium
			) {
				actions()
			}
		}
		if (onClick != null || showSuffixIcon) {
			Spacer(modifier = Modifier.width(16.dp))
			NoIcon(
				icon = ComposeIcon.ArrowForwardIos.value(),
				modifier = Modifier.size(20.dp),
				tint = MaterialTheme.colorScheme.outline
			)
		}
	}
}