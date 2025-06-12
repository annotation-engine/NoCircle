package com.nocircle.common.resources

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
class IconGroup internal constructor(
	internal val rounded: () -> ImageVector,
	internal val outlined: () -> ImageVector,
	internal val filled: () -> ImageVector,
	internal val sharp: () -> ImageVector,
	internal val twoTone: () -> ImageVector
)

fun iconGroup(
	rounded: Icons.Rounded.() -> ImageVector,
	outlined: Icons.Outlined.() -> ImageVector,
	filled: Icons.Filled.() -> ImageVector,
	sharp: Icons.Sharp.() -> ImageVector,
	twoTone: Icons.TwoTone.() -> ImageVector
): IconGroup = IconGroup(
	rounded = { Icons.Rounded.rounded() },
	outlined = { Icons.Outlined.outlined() },
	filled = { Icons.Filled.filled() },
	sharp = { Icons.Sharp.sharp() },
	twoTone = { Icons.TwoTone.twoTone() }
)

fun autoMirroredIconGroup(
	rounded: Icons.AutoMirrored.Rounded.() -> ImageVector,
	outlined: Icons.AutoMirrored.Outlined.() -> ImageVector,
	filled: Icons.AutoMirrored.Filled.() -> ImageVector,
	sharp: Icons.AutoMirrored.Sharp.() -> ImageVector,
	twoTone: Icons.AutoMirrored.TwoTone.() -> ImageVector
): IconGroup = IconGroup(
	rounded = { Icons.AutoMirrored.Rounded.rounded() },
	outlined = { Icons.AutoMirrored.Outlined.outlined() },
	filled = { Icons.AutoMirrored.Filled.filled() },
	sharp = { Icons.AutoMirrored.Sharp.sharp() },
	twoTone = { Icons.AutoMirrored.TwoTone.twoTone() }
)

@Composable
fun IconGroup.value(): ImageVector = value(IconType.current)

fun IconGroup.getIcon(): ImageVector = this.value(IconType.value)

@Stable
private fun IconGroup.value(iconType: IconType) = when (iconType) {
	IconType.ROUNDED -> rounded()
	IconType.OUTLINED -> outlined()
	IconType.FILLED -> filled()
	IconType.SHARP -> sharp()
	IconType.TWO_TONE -> twoTone()
}