package com.nocircle.common.resources

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

typealias RoundedIcon = Icons.Rounded.() -> ImageVector
typealias OutlinedIcon = Icons.Outlined.() -> ImageVector
typealias FilledIcon = Icons.Filled.() -> ImageVector
typealias SharpIcon = Icons.Sharp.() -> ImageVector
typealias TwoToneIcon = Icons.TwoTone.() -> ImageVector

@Immutable
interface NoIcon {
	val rounded: RoundedIcon
	val outlined: OutlinedIcon
	val filled: FilledIcon
	val sharp: SharpIcon
	val twoTone: TwoToneIcon
}

@Composable
fun NoIcon.value(): ImageVector = value(IconType.current)

fun NoIcon.getIcon(): ImageVector = this.value(IconType.value)

@Stable
private fun NoIcon.value(iconType: IconType) = when (iconType) {
	IconType.Rounded -> Icons.Rounded.rounded()
	IconType.Outlined -> Icons.Outlined.outlined()
	IconType.Filled -> Icons.Filled.filled()
	IconType.Sharp -> Icons.Sharp.sharp()
	IconType.TwoTone -> Icons.TwoTone.twoTone()
}