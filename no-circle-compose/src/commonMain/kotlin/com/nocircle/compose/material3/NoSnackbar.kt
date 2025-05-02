package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nocircle.common.material3.NoSnackbarColors.*
import com.nocircle.common.material3.NoSnackbarVisuals
import com.nocircle.compose.foundation.NoIcon

@Composable
fun NoSnackbar(
	snackbarData: SnackbarData,
	modifier: Modifier = Modifier,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	shape: Shape = MaterialTheme.shapes.medium,
	containerColor: Color = MaterialTheme.colorScheme.primary,
	contentColor: Color = contentColorFor(containerColor),
	prefixIconColor: Color = contentColorFor(containerColor),
	actionColor: Color = MaterialTheme.colorScheme.primaryContainer,
	actionContentColor: Color = contentColorFor(actionColor),
	dismissActionContentColor: Color = contentColorFor(containerColor)
) {
	val visuals = snackbarData.visuals as? NoSnackbarVisuals ?: return
	val containerColor = when (visuals.colors) {
		Primary -> MaterialTheme.colorScheme.primary
		Secondary -> MaterialTheme.colorScheme.secondary
		Tertiary -> MaterialTheme.colorScheme.tertiary
		Error -> MaterialTheme.colorScheme.error
		Surface -> MaterialTheme.colorScheme.surface
		null -> containerColor
	}
	val contentColor = if (visuals.colors != null) {
		contentColorFor(containerColor)
	} else contentColor
	val prefixIconColor = if (visuals.colors != null) {
		contentColorFor(containerColor)
	} else prefixIconColor
	val actionColor = when (visuals.colors) {
		Primary -> MaterialTheme.colorScheme.primaryContainer
		Secondary -> MaterialTheme.colorScheme.secondaryContainer
		Tertiary -> MaterialTheme.colorScheme.tertiaryContainer
		Error -> MaterialTheme.colorScheme.errorContainer
		Surface -> MaterialTheme.colorScheme.surfaceContainer
		null -> actionColor
	}
	val actionContentColor = if (visuals.colors != null) {
		contentColorFor(actionColor)
	} else actionContentColor
	val dismissActionContentColor = if (visuals.colors != null) {
		contentColorFor(containerColor)
	} else dismissActionContentColor
	
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(24.dp)
			.shadow(
				elevation = 2.dp,
				shape = shape
			)
			.clip(shape)
			.background(
				color = containerColor,
				shape = shape
			)
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		if (visuals.prefixIcon != null) {
			NoIcon(
				icon = visuals.prefixIcon!!,
				modifier = Modifier.size(24.dp),
				tint = prefixIconColor
			)
			Spacer(modifier = Modifier.width(8.dp))
		}
		Text(
			text = visuals.message,
			modifier = Modifier
				.weight(1f),
			fontSize = 16.sp,
			color = contentColor,
			lineHeight = 24.sp,
			overflow = TextOverflow.Ellipsis,
			maxLines = maxLines,
		)
		if (visuals.actionLabel != null) {
			Spacer(modifier = Modifier.width(8.dp))
			Box(
				modifier = Modifier
					.clip(MaterialTheme.shapes.small)
					.background(
						color = actionColor,
						shape = MaterialTheme.shapes.small
					)
					.clickable {
						snackbarData.performAction()
					}
					.padding(
						horizontal = 10.dp,
						vertical = 4.dp
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = visuals.actionLabel!!,
					fontSize = 15.sp,
					color = actionContentColor
				)
			}
		}
		val showDismissAction by remember(visuals.withDismissAction, visuals.actionLabel, visuals.duration) {
			derivedStateOf {
				visuals.withDismissAction || (visuals.actionLabel == null && visuals.duration == SnackbarDuration.Indefinite)
			}
		}
		if (showDismissAction) {
			Spacer(modifier = Modifier.width(8.dp))
			Icon(
				imageVector = Icons.Rounded.Cancel,
				contentDescription = null,
				modifier = Modifier
					.size(24.dp)
					.clickable {
						snackbarData.dismiss()
					},
				tint = dismissActionContentColor
			)
		}
	}
}