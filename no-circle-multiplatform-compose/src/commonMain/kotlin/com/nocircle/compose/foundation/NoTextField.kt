@file:Suppress("ConstPropertyName")

package com.nocircle.compose.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTextField(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	readOnly: Boolean = false,
	textStyle: TextStyle = LocalTextStyle.current,
	placeholder: @Composable (() -> Unit)? = null,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	prefix: @Composable (() -> Unit)? = null,
	suffix: @Composable (() -> Unit)? = null,
	supportingText: @Composable (() -> Unit)? = null,
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	minLines: Int = 1,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	shape: Shape = NoTextFieldDefaults.shape,
	colors: TextFieldColors = NoTextFieldDefaults.colors()
) {
	val textColor =
		textStyle.color.takeOrElse {
			val focused = interactionSource.collectIsFocusedAsState().value
			colors.textColor(enabled, isError, focused)
		}
	val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
	CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
		BasicTextField(
			value = value,
			modifier = modifier
				.height(52.dp)
				.defaultMinSize(
					minWidth = NoTextFieldDefaults.MinWidth,
					minHeight = NoTextFieldDefaults.MinHeight
				),
			onValueChange = onValueChange,
			enabled = enabled,
			readOnly = readOnly,
			textStyle = mergedTextStyle,
			cursorBrush = SolidColor(colors.cursorColor(isError)),
			visualTransformation = visualTransformation,
			keyboardOptions = keyboardOptions,
			keyboardActions = keyboardActions,
			interactionSource = interactionSource,
			singleLine = singleLine,
			maxLines = maxLines,
			minLines = minLines,
			decorationBox = @Composable { innerTextField ->
				OutlinedTextFieldDefaults.DecorationBox(
					value = value,
					visualTransformation = visualTransformation,
					innerTextField = innerTextField,
					placeholder = placeholder,
					leadingIcon = leadingIcon,
					trailingIcon = trailingIcon,
					prefix = prefix,
					suffix = suffix,
					supportingText = supportingText,
					singleLine = singleLine,
					enabled = enabled,
					isError = isError,
					interactionSource = interactionSource,
					colors = colors,
					contentPadding = NoTextFieldDefaults.contentPadding,
					container = {
						Container(
							enabled = enabled,
							isError = isError,
							interactionSource = interactionSource,
							colors = colors,
							shape = shape,
						)
					}
				)
			}
		)
	}
}

@Composable
private fun Container(
	enabled: Boolean,
	isError: Boolean,
	interactionSource: InteractionSource,
	modifier: Modifier = Modifier,
	colors: TextFieldColors,
	shape: Shape,
	focusedBorderThickness: Dp = NoTextFieldDefaults.FocusedBorderThickness,
	unfocusedBorderThickness: Dp = NoTextFieldDefaults.UnfocusedBorderThickness,
) {
	val focused by interactionSource.collectIsFocusedAsState()
	val borderStroke by animateBorderStrokeAsState(
		enabled,
		isError,
		focused,
		colors,
		focusedBorderThickness,
		unfocusedBorderThickness
	)
	val containerColor = animateColorAsState(
		targetValue = colors.containerColor(enabled, isError, focused),
		animationSpec = tween(durationMillis = NoTextFieldDefaults.TextFieldAnimationDuration)
	)
	Box(
		modifier = modifier
			.border(
				border = borderStroke,
				shape = shape
			)
			.textFieldBackground(containerColor::value, shape)
	)
}

@Stable
private fun TextFieldColors.cursorColor(isError: Boolean): Color =
	if (isError) errorCursorColor else cursorColor

@Stable
private fun TextFieldColors.textColor(
	enabled: Boolean,
	isError: Boolean,
	focused: Boolean
): Color = when {
	!enabled -> disabledTextColor
	isError -> errorTextColor
	focused -> focusedTextColor
	else -> unfocusedTextColor
}

@Stable
private fun TextFieldColors.containerColor(
	enabled: Boolean,
	isError: Boolean,
	focused: Boolean,
): Color = when {
	!enabled -> disabledContainerColor
	isError -> errorContainerColor
	focused -> focusedContainerColor
	else -> unfocusedContainerColor
}

@Composable
private fun animateBorderStrokeAsState(
	enabled: Boolean,
	isError: Boolean,
	focused: Boolean,
	colors: TextFieldColors,
	focusedBorderThickness: Dp,
	unfocusedBorderThickness: Dp
): State<BorderStroke> {
	val targetColor = colors.indicatorColor(enabled, isError, focused)
	val indicatorColor =
		if (enabled) {
			animateColorAsState(targetColor, tween(durationMillis = NoTextFieldDefaults.TextFieldAnimationDuration))
		} else {
			rememberUpdatedState(targetColor)
		}
	
	val thickness =
		if (enabled) {
			val targetThickness = if (focused) focusedBorderThickness else unfocusedBorderThickness
			animateDpAsState(targetThickness, tween(durationMillis = NoTextFieldDefaults.TextFieldAnimationDuration))
		} else {
			rememberUpdatedState(unfocusedBorderThickness)
		}
	
	return rememberUpdatedState(BorderStroke(thickness.value, indicatorColor.value))
}

@Stable
private fun TextFieldColors.indicatorColor(
	enabled: Boolean,
	isError: Boolean,
	focused: Boolean,
): Color = when {
	!enabled -> disabledIndicatorColor
	isError -> errorIndicatorColor
	focused -> focusedIndicatorColor
	else -> unfocusedIndicatorColor
}

@Stable
private fun Modifier.textFieldBackground(
	color: ColorProducer,
	shape: Shape,
): Modifier = this.drawWithCache {
	val outline = shape.createOutline(size, layoutDirection, this)
	onDrawBehind { drawOutline(outline, color = color()) }
}

@Immutable
object NoTextFieldDefaults {
	
	val MinHeight = 40.dp
	
	val MinWidth = 160.dp
	
	const val TextFieldAnimationDuration = 150
	
	val shape: Shape
		@Composable
		get() = MaterialTheme.shapes.small
	
	val contentPadding = PaddingValues(horizontal = 8.dp)
	
	val FocusedBorderThickness = 1.5.dp
	
	val UnfocusedBorderThickness = FocusedBorderThickness
	
	@Composable
	fun colors() = OutlinedTextFieldDefaults.colors(
		focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0f),
		unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
		focusedBorderColor = MaterialTheme.colorScheme.primary,
		unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0f),
		unfocusedPrefixColor = MaterialTheme.colorScheme.outline,
		unfocusedSuffixColor = MaterialTheme.colorScheme.outline,
		unfocusedTextColor = MaterialTheme.colorScheme.outline,
		unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
		unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline
	)
}