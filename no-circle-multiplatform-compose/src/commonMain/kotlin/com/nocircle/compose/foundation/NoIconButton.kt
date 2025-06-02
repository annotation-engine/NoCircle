package com.nocircle.compose.foundation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun NoIconButton(
	icon: ImageVector,
	contentDescription: String? = null,
	modifier: Modifier = Modifier,
	tint: Color = LocalContentColor.current,
	shape: Shape = MaterialTheme.shapes.small,
	contentPadding: PaddingValues = PaddingValues(8.dp),
	containerColor: Color = Color.Transparent,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	context: CoroutineContext = EmptyCoroutineContext,
	enabled: Boolean = true,
	onClick: suspend () -> Unit,
) {
	val coroutineScope = rememberCoroutineScope()
	Icon(
		imageVector = icon,
		contentDescription = contentDescription,
		modifier = modifier
			.clip(shape)
			.background(
				color = containerColor,
				shape = shape,
			)
			.clickable(
				interactionSource = interactionSource,
				indication = LocalIndication.current,
				enabled = enabled,
				onClick = {
					coroutineScope.launch(context) {
						onClick()
					}
				}
			)
			.pointerHoverIcon(PointerIcon.Default)
			.padding(contentPadding),
		tint = tint
	)
}