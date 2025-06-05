package com.nocircle.common.resources

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
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

val NoIcon.value: ImageVector
	@Composable
	get() {
		val iconType by IconType.current.collectAsState()
		return value(iconType)
	}

fun NoIcon.getIcon(): ImageVector = this.value(IconType.current.value)

@Stable
private fun NoIcon.value(iconType: IconType) = when (iconType) {
	IconType.Rounded -> Icons.Rounded.rounded()
	IconType.Outlined -> Icons.Outlined.outlined()
	IconType.Filled -> Icons.Filled.filled()
	IconType.Sharp -> Icons.Sharp.sharp()
	IconType.TwoTone -> Icons.TwoTone.twoTone()
}