package com.nocircle.compose.foundation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun NoButton(
	text: String,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 18.sp,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	fontWeight: FontWeight? = null,
	colors: NoButtonColors = NoButtonDefaults.DefaultButtonColors,
	context: CoroutineContext = EmptyCoroutineContext,
	onClick: (suspend CoroutineScope.() -> Unit)? = null,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isPressed by interactionSource.collectIsPressedAsState()
	val shadowElevation by animateDpAsState(
		targetValue = if (isPressed) 4.dp else 2.dp
	)
	val coroutineScope = rememberCoroutineScope()
	Button(
		onClick = {
			if (onClick != null) {
				coroutineScope.launch(
					context = context,
					block = onClick
				)
			}
		},
		modifier = modifier
			.height(56.dp)
			.shadow(
				elevation = shadowElevation,
				shape = MaterialTheme.shapes.medium,
				ambientColor = DefaultShadowColor.copy(alpha = 0.6f),
				spotColor = DefaultShadowColor.copy(alpha = 0.6f)
			),
		shape = MaterialTheme.shapes.medium,
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
		disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
	): NoButtonColors = NoButtonColors(containerColor, contentColor, disabledContainerColor, disabledContentColor)
	
	val PrimaryColors: NoButtonColors
		@Composable
		get() = colors()
	
	val PrimaryContainerColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.primaryContainer,
			contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
			disabledContentColor = MaterialTheme.colorScheme.primaryContainer.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
		)
	
	val SecondaryColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.secondary,
			contentColor = MaterialTheme.colorScheme.onSecondary,
			disabledContentColor = MaterialTheme.colorScheme.secondary.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onSecondary.copy(0.8f)
		)
	
	val SecondaryContainerColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer,
			contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			disabledContentColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.8f)
		)
	
	val TertiaryColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.tertiary,
			contentColor = MaterialTheme.colorScheme.onTertiary,
			disabledContentColor = MaterialTheme.colorScheme.tertiary.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onTertiary.copy(0.8f)
		)
	
	val TertiaryContainerColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer,
			contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
			disabledContentColor = MaterialTheme.colorScheme.tertiaryContainer.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(0.8f)
		)
	
	val ErrorColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.error,
			contentColor = MaterialTheme.colorScheme.onError,
			disabledContentColor = MaterialTheme.colorScheme.error.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onError.copy(0.8f)
		)
	
	val ErrorContainerColors: NoButtonColors
		@Composable
		get() = colors(
			containerColor = MaterialTheme.colorScheme.errorContainer,
			contentColor = MaterialTheme.colorScheme.onErrorContainer,
			disabledContentColor = MaterialTheme.colorScheme.errorContainer.copy(0.8f),
			disabledContainerColor = MaterialTheme.colorScheme.onErrorContainer.copy(0.8f)
		)
}

object NoButtonDefaults {
	
	val DefaultButtonColors @Composable get() = NoButtons.colors()
}

class NoButtonColors internal constructor(
	val containerColor: Color,
	val contentColor: Color,
	val disabledContainerColor: Color,
	val disabledContentColor: Color,
)