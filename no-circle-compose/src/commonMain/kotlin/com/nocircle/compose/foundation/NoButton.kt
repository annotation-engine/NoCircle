package com.nocircle.compose.foundation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoButton(
	text: String,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 18.sp,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	fontWeight: FontWeight? = null,
	colors: NoButtonColors = NoButtonDefaults.DefaultButtonColors,
	onClick: (() -> Unit)? = null
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isPressed by interactionSource.collectIsPressedAsState()
	val shadowElevation by animateDpAsState(
		targetValue = if (isPressed) 4.dp else 2.dp
	)
	Button(
		onClick = onClick ?: {},
		modifier = modifier
			.height(56.dp)
			.shadow(
				elevation = shadowElevation,
				shape = MaterialTheme.shapes.large,
				ambientColor = DefaultShadowColor.copy(alpha = 0.6f),
				spotColor = DefaultShadowColor.copy(alpha = 0.6f)
			),
		shape = RoundedCornerShape(16.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = colors.containerColor,
			contentColor = colors.contentColor,
			disabledContainerColor = colors.disabledContainerColor,
			disabledContentColor = colors.disabledContentColor
		),
		interactionSource = interactionSource
	) {
		Text(
			text = text,
			fontSize = fontSize,
			fontWeight = fontWeight,
			letterSpacing = letterSpacing
		)
	}
}

object NoButtons {
	
	@Composable
	fun colors(
		containerColor: Color = MaterialTheme.colorScheme.primary,
		contentColor: Color = MaterialTheme.colorScheme.onPrimary,
		disabledContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
		disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
	): NoButtonColors = NoButtonColors(containerColor, contentColor, disabledContainerColor, disabledContentColor)
}

object NoButtonDefaults {
	
	val DefaultButtonColors @Composable get() = NoButtons.colors()
}

class NoButtonColors internal constructor(
	val containerColor: Color,
	val contentColor: Color,
	val disabledContainerColor: Color,
	val disabledContentColor: Color
)