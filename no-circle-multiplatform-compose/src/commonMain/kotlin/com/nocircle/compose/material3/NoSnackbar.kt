package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nocircle.common.resources.getIcon
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoSnackbarColors.*
import com.nocircle.compose.resources.ComposeIcon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@Composable
fun NoSnackbar(
	snackbarData: SnackbarData,
	modifier: Modifier = Modifier,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	shape: Shape = MaterialTheme.shapes.small,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
	contentColor: Color = contentColorFor(containerColor),
	prefixIconColor: Color = contentColorFor(containerColor),
	actionColor: Color = MaterialTheme.colorScheme.secondary,
	actionContentColor: Color = contentColorFor(actionColor),
	dismissActionContentColor: Color = contentColorFor(containerColor),
) {
	val visuals = snackbarData.visuals as? NoSnackbarVisuals ?: return
	val containerColor = when (visuals.colors) {
		PRIMARY -> MaterialTheme.colorScheme.primaryContainer
		SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
		TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
		ERROR -> MaterialTheme.colorScheme.errorContainer
		SURFACE -> MaterialTheme.colorScheme.surfaceContainer
		null -> containerColor
	}
	val contentColor = visuals.colors?.let { contentColorFor(containerColor) } ?: contentColor
	val prefixIconColor = visuals.colors?.let { contentColorFor(containerColor) } ?: prefixIconColor
	val actionColor = when (visuals.colors) {
		PRIMARY -> MaterialTheme.colorScheme.primaryContainer
		SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
		TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
		ERROR -> MaterialTheme.colorScheme.errorContainer
		SURFACE -> MaterialTheme.colorScheme.surfaceContainer
		null -> actionColor
	}
	val actionContentColor = visuals.colors?.let { contentColorFor(actionColor) } ?: actionContentColor
	val dismissActionContentColor = visuals.colors?.let { contentColorFor(actionColor) } ?: dismissActionContentColor
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(24.dp)
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
				icon = visuals.prefixIcon,
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
					text = visuals.actionLabel,
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
				imageVector = ComposeIcon.Cancel.getIcon(),
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

@Immutable
@ConsistentCopyVisibility
data class NoSnackbarVisuals internal constructor(
	override val message: String,
	override val actionLabel: String?,
	val prefixIcon: ImageVector?,
	override val withDismissAction: Boolean,
	override val duration: SnackbarDuration,
	val colors: NoSnackbarColors?
) : SnackbarVisuals

enum class NoSnackbarColors {
	PRIMARY,
	SECONDARY,
	TERTIARY,
	ERROR,
	SURFACE
}

suspend fun SnackbarHostState.showNoSnackbar(
	visuals: NoSnackbarVisuals
): SnackbarResult = showSnackbar(visuals)

suspend fun SnackbarHostState.showNoSnackbar(
	message: String,
	actionLabel: String? = null,
	prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = SnackbarDuration.Short,
	colors: NoSnackbarColors? = null,
): SnackbarResult = showSnackbar(NoSnackbarVisuals(message, actionLabel, prefixIcon, withDismissAction, duration, colors))

suspend fun SnackbarHostState.showNoSnackbar(
	message: StringResource,
	actionLabel: String? = null,
	prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = SnackbarDuration.Short,
	colors: NoSnackbarColors? = null,
): SnackbarResult = showSnackbar(NoSnackbarVisuals(getString(message), actionLabel, prefixIcon, withDismissAction, duration, colors))