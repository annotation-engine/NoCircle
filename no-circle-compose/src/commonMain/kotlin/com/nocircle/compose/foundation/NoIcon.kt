package com.nocircle.compose.foundation

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun NoIcon(
	icon: ImageVector,
	contentDescription: String? = null,
	modifier: Modifier = Modifier,
	tint: Color = LocalNoIconTintColor.current,
	context: CoroutineContext = EmptyCoroutineContext,
	onClick: (suspend CoroutineScope.() -> Unit)? = null,
) {
	if (onClick != null) {
		val coroutineScope = rememberCoroutineScope()
		IconButton(
			onClick = {
				coroutineScope.launch(
					context = context,
					block = onClick
				)
			}
		) {
			Icon(
				imageVector = icon,
				contentDescription = contentDescription,
				modifier = modifier,
				tint = tint
			)
		}
	} else {
		Icon(
			imageVector = icon,
			contentDescription = contentDescription,
			modifier = modifier,
			tint = tint
		)
	}
}

val LocalNoIconTintColor = compositionLocalOf { Color.Black }