@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.compose.foundation

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
inline fun NoIcon(
	icon: ImageVector,
	contentDescription: String? = null,
	modifier: Modifier = Modifier,
	tint: Color = LocalContentColor.current
) {
	Icon(
		imageVector = icon,
		contentDescription = contentDescription,
		modifier = modifier,
		tint = tint
	)
}