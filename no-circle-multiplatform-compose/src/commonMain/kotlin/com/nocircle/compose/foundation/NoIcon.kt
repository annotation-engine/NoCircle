package com.nocircle.compose.foundation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
	shape: Shape = MaterialTheme.shapes.small,
	enabled: Boolean = true,
	context: CoroutineContext = EmptyCoroutineContext,
	onClick: (suspend CoroutineScope.() -> Unit)? = null
) {
	val iconContent: @Composable () -> Unit = {
		Icon(
			imageVector = icon,
			contentDescription = contentDescription,
			modifier = modifier,
			tint = tint
		)
	}
	if (onClick != null) {
		val coroutineScope = rememberCoroutineScope()
		Box(
			modifier = Modifier
				.clip(shape)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = LocalIndication.current,
					enabled = enabled,
					onClick = {
						coroutineScope.launch(context) {
							onClick()
						}
					}
				)
				.padding(8.dp)
		) {
			iconContent()
		}
	} else {
		iconContent()
	}
}

val LocalNoIconTintColor = compositionLocalOf { Color.Black }