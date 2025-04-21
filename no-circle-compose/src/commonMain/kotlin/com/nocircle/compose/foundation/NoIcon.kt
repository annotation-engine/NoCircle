package com.nocircle.compose.foundation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NoIcon(
	icon: ImageVector,
	contentDescription: String? = null,
	modifier: Modifier = Modifier,
	tint: Color = LocalNoIconTintColor.current
) {
	Icon(
		imageVector = icon,
		contentDescription = contentDescription,
		modifier = modifier,
		tint = tint
	)
}

val LocalNoIconTintColor = compositionLocalOf { Color.Black }