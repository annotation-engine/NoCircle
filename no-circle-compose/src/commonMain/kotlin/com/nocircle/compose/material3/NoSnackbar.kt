package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nocircle.compose.foundation.NoIcon

@Composable
fun NoSnackbar(
	snackbarData: SnackbarData,
	modifier: Modifier = Modifier,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	shape: Shape = MaterialTheme.shapes.small,
	colors: NoSnackbarColors = NoSnackbars.Primary,
) {
	val visuals = snackbarData.visuals as? NoSnackbarVisuals ?: return
	val colors = visuals.colors ?: colors
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
				color = colors.containerColor,
				shape = shape
			)
			.padding(12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		if (visuals.prefixIcon != null) {
			NoIcon(
				icon = visuals.prefixIcon,
				modifier = Modifier.size(24.dp),
				tint = colors.prefixIconColor
			)
			Spacer(modifier = Modifier.width(8.dp))
		}
		Text(
			text = visuals.message,
			modifier = Modifier
				.weight(1f),
			fontSize = 15.sp,
			color = colors.contentColor,
			lineHeight = 24.sp,
			overflow = TextOverflow.Ellipsis,
			maxLines = maxLines,
		)
		if (visuals.actionLabel != null) {
			Spacer(modifier = Modifier.width(8.dp))
			Box(
				modifier = Modifier
					.clip(MaterialTheme.shapes.extraSmall)
					.background(
						color = colors.actionColor,
						shape = MaterialTheme.shapes.extraSmall
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
					fontSize = 14.sp,
					color = colors.actionContentColor
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
				tint = colors.dismissActionContentColor
			)
		}
	}
}

object NoSnackbars {
	
	@Composable
	fun colors(
		containerColor: Color = MaterialTheme.colorScheme.primary,
		contentColor: Color = MaterialTheme.colorScheme.onPrimary,
		prefixIconColor: Color = MaterialTheme.colorScheme.onPrimary,
		actionColor: Color = MaterialTheme.colorScheme.primaryContainer,
		actionContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
		dismissActionContentColor: Color = MaterialTheme.colorScheme.onPrimary,
	): NoSnackbarColors = NoSnackbarColors(containerColor, contentColor, prefixIconColor, actionColor, actionContentColor, dismissActionContentColor)
	
	val Primary: NoSnackbarColors
		@Composable
		get() = colors()
	
	val Secondary: NoSnackbarColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.secondary,
			contentColor = MaterialTheme.colorScheme.onSecondary,
			prefixIconColor = MaterialTheme.colorScheme.onSecondary,
			actionColor = MaterialTheme.colorScheme.secondaryContainer,
			actionContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			dismissActionContentColor = MaterialTheme.colorScheme.onSecondary
		)
	
	val Tertiary: NoSnackbarColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.tertiary,
			contentColor = MaterialTheme.colorScheme.onTertiary,
			prefixIconColor = MaterialTheme.colorScheme.onTertiary,
			actionColor = MaterialTheme.colorScheme.tertiaryContainer,
			actionContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
			dismissActionContentColor = MaterialTheme.colorScheme.onTertiary
		)
	
	val Error: NoSnackbarColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.error,
			contentColor = MaterialTheme.colorScheme.onError,
			prefixIconColor = MaterialTheme.colorScheme.onError,
			actionColor = MaterialTheme.colorScheme.errorContainer,
			actionContentColor = MaterialTheme.colorScheme.onErrorContainer,
			dismissActionContentColor = MaterialTheme.colorScheme.onError
		)
}

data class NoSnackbarColors(
	val containerColor: Color,
	val contentColor: Color,
	val prefixIconColor: Color,
	val actionColor: Color,
	val actionContentColor: Color,
	val dismissActionContentColor: Color,
)

@ConsistentCopyVisibility
data class NoSnackbarVisuals internal constructor(
	override val message: String,
	override val actionLabel: String?,
	val prefixIcon: ImageVector?,
	override val withDismissAction: Boolean,
	override val duration: SnackbarDuration,
	val colors: NoSnackbarColors?,
) : SnackbarVisuals

suspend fun SnackbarHostState.showNoSnackbar(
	message: String,
	actionLabel: String? = null,
	prefixIcon: ImageVector? = null,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = SnackbarDuration.Short,
	colors: NoSnackbarColors? = null,
): SnackbarResult = this.showSnackbar(NoSnackbarVisuals(message, actionLabel, prefixIcon, withDismissAction, duration, colors))