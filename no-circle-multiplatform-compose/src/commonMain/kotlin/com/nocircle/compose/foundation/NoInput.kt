package com.nocircle.compose.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoInput(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	readOnly: Boolean = false,
	placeholder: String? = null,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	prefix: @Composable (() -> Unit)? = null,
	suffix: @Composable (() -> Unit)? = null,
	supportingText: @Composable (() -> Unit)? = null,
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
	val focusedColor = MaterialTheme.colorScheme.primary
	val unfocusedColor = MaterialTheme.colorScheme.outline
	val isHovered by interactionSource.collectIsHoveredAsState()
	val isFocused by interactionSource.collectIsFocusedAsState()
	val showIcon by remember(isHovered, isFocused) {
		derivedStateOf { isHovered || isFocused }
	}
	val shadowElevation by animateDpAsState(
		targetValue = if (isHovered) 4.dp else 2.dp
	)
	val textStyle = LocalTextStyle.current.copy(
		fontSize = 16.sp,
		lineHeight = 24.sp
	)
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier
			.fillMaxWidth()
			.height(56.dp)
			.shadow(
				elevation = shadowElevation,
				shape = MaterialTheme.shapes.small,
				ambientColor = DefaultShadowColor.copy(alpha = 0.6f),
				spotColor = DefaultShadowColor.copy(alpha = 0.6f)
			)
			.hoverable(interactionSource),
		enabled = enabled,
		readOnly = readOnly,
		textStyle = textStyle,
		placeholder = placeholder?.let {
			{
				Text(
					text = it,
					style = textStyle.copy(
						color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
					)
				)
			}
		},
		leadingIcon = leadingIcon?.let {
			{
				val tintColor by animateColorAsState(
					targetValue = if (isHovered) focusedColor else unfocusedColor
				)
				CompositionLocalProvider(
					LocalNoIconTintColor provides tintColor,
					content = it
				)
			}
		},
		trailingIcon = trailingIcon?.let {
			{ if (showIcon) it() }
		},
		prefix = prefix,
		suffix = suffix,
		supportingText = supportingText,
		isError = isError,
		visualTransformation = visualTransformation,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
		singleLine = true,
		interactionSource = interactionSource,
		shape = MaterialTheme.shapes.small,
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor = focusedColor,
			unfocusedBorderColor = unfocusedColor,
			focusedContainerColor = MaterialTheme.colorScheme.surface,
			unfocusedContainerColor = MaterialTheme.colorScheme.surface,
			selectionColors = TextSelectionColors(
				handleColor = focusedColor,
				backgroundColor = focusedColor.copy(alpha = 0.2f)
			)
		),
	)
}