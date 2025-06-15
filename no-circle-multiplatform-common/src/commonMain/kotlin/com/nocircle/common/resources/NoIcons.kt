package com.nocircle.common.resources

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
class NoIcons internal constructor(
	internal val rounded: () -> ImageVector,
	internal val outlined: () -> ImageVector,
	internal val filled: () -> ImageVector,
	internal val sharp: () -> ImageVector,
	internal val twoTone: () -> ImageVector
)

fun icons(
	rounded: Icons.Rounded.() -> ImageVector,
	outlined: Icons.Outlined.() -> ImageVector,
	filled: Icons.Filled.() -> ImageVector,
	sharp: Icons.Sharp.() -> ImageVector,
	twoTone: Icons.TwoTone.() -> ImageVector
): NoIcons = NoIcons(
	rounded = { Icons.Rounded.rounded() },
	outlined = { Icons.Outlined.outlined() },
	filled = { Icons.Filled.filled() },
	sharp = { Icons.Sharp.sharp() },
	twoTone = { Icons.TwoTone.twoTone() }
)

fun autoMirroredIcons(
	rounded: Icons.AutoMirrored.Rounded.() -> ImageVector,
	outlined: Icons.AutoMirrored.Outlined.() -> ImageVector,
	filled: Icons.AutoMirrored.Filled.() -> ImageVector,
	sharp: Icons.AutoMirrored.Sharp.() -> ImageVector,
	twoTone: Icons.AutoMirrored.TwoTone.() -> ImageVector
): NoIcons = NoIcons(
	rounded = { Icons.AutoMirrored.Rounded.rounded() },
	outlined = { Icons.AutoMirrored.Outlined.outlined() },
	filled = { Icons.AutoMirrored.Filled.filled() },
	sharp = { Icons.AutoMirrored.Sharp.sharp() },
	twoTone = { Icons.AutoMirrored.TwoTone.twoTone() }
)

@Composable
fun NoIcons.value(): ImageVector = value(NoIconType.current)

fun NoIcons.getIcon(): ImageVector = this.value(NoIconType.value)

@Stable
private fun NoIcons.value(iconType: NoIconType) = when (iconType) {
	NoIconType.ROUNDED -> rounded()
	NoIconType.OUTLINED -> outlined()
	NoIconType.FILLED -> filled()
	NoIconType.SHARP -> sharp()
	NoIconType.TWO_TONE -> twoTone()
}