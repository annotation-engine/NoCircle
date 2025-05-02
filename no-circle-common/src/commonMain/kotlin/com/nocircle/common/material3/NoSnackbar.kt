package com.nocircle.common.material3

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.vector.ImageVector
import com.nocircle.common.expends.value
import org.jetbrains.compose.resources.StringResource

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
	Primary,
	Secondary,
	Tertiary,
	Error,
	Surface
}

suspend fun SnackbarHostState.showNoSnackbar(
	visuals: NoSnackbarVisuals
): SnackbarResult = showSnackbar(visuals)

suspend fun SnackbarHostState.showNoSnackbar(
	message: String,
	actionLabel: String? = null,
	prefixIcon: ImageVector? = Icons.Rounded.Info,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = SnackbarDuration.Short,
	colors: NoSnackbarColors? = null,
): SnackbarResult = showSnackbar(NoSnackbarVisuals(message, actionLabel, prefixIcon, withDismissAction, duration, colors))

suspend fun SnackbarHostState.showNoSnackbar(
	message: StringResource,
	actionLabel: String? = null,
	prefixIcon: ImageVector? = Icons.Rounded.Info,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = SnackbarDuration.Short,
	colors: NoSnackbarColors? = null,
): SnackbarResult = showSnackbar(NoSnackbarVisuals(message.value(), actionLabel, prefixIcon, withDismissAction, duration, colors))