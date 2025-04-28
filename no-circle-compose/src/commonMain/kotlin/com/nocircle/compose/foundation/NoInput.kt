package com.nocircle.compose.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
	val focusedColor = MaterialTheme.colorScheme.primary
	val unfocusedColor = MaterialTheme.colorScheme.outline
	val isFocused by interactionSource.collectIsFocusedAsState()
	val shadowElevation by animateDpAsState(
		targetValue = if (isFocused) 4.dp else 2.dp
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
				shape = MaterialTheme.shapes.medium,
				ambientColor = DefaultShadowColor.copy(alpha = 0.6f),
				spotColor = DefaultShadowColor.copy(alpha = 0.6f)
			),
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
					targetValue = if (isFocused) focusedColor else unfocusedColor
				)
				CompositionLocalProvider(
					LocalNoIconTintColor provides tintColor,
					content = it
				)
			}
		},
		trailingIcon = trailingIcon?.let {
			{ if (isFocused) it() }
		},
		isError = isError,
		visualTransformation = visualTransformation,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
		singleLine = true,
		interactionSource = interactionSource,
		shape = MaterialTheme.shapes.medium,
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor = focusedColor,
			unfocusedBorderColor = unfocusedColor,
			focusedContainerColor = MaterialTheme.colorScheme.surface,
			unfocusedContainerColor = MaterialTheme.colorScheme.surface,
		),
	)
}