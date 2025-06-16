package com.nocircle.compose.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NoTag(
	text: String,
	icon: ImageVector? = null,
	modifier: Modifier = Modifier,
	iconSize: Dp = NoTagDefaults.IconSize,
	color: Color = MaterialTheme.colorScheme.primary,
	shape: Shape = MaterialTheme.shapes.small,
	style: TextStyle = MaterialTheme.typography.labelMedium,
	contentPadding: PaddingValues = NoTagDefaults.ContentPadding,
	type: NoTagType = NoTagType.Button
) {
	Row(
		modifier = modifier
			.clip(shape)
			.then(
				other = when (type) {
					is NoTagType.Button -> {
						Modifier.background(
							color = color,
							shape = shape
						)
					}
					
					is NoTagType.Border -> {
						Modifier
							.border(
								width = type.borderWidth,
								color = color,
								shape = shape
							)
							.background(
								color = color.copy(alpha = type.backgroundColorAlpha),
								shape = shape
							)
					}
				}
			)
			.padding(contentPadding),
		verticalAlignment = Alignment.CenterVertically
	) {
		val color by remember(type, color) {
			derivedStateOf {
				when (type) {
					is NoTagType.Button -> if (color.luminance() > 0.5f) Color.Black else Color.White
					is NoTagType.Border -> color
				}
			}
		}
		if (icon != null) {
			NoIcon(
				icon = icon,
				modifier = Modifier.size(iconSize),
				tint = color
			)
			Spacer(modifier = Modifier.width(8.dp))
		}
		Text(
			text = text,
			color = color,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = style,
		)
	}
}

object NoTagDefaults {
	
	val ContentPadding = PaddingValues(12.dp, vertical = 6.dp)
	
	val IconSize = 24.dp
}

@Immutable
sealed interface NoTagType {
	
	@Immutable
	object Button : NoTagType
	
	@Immutable
	class Border(
		val borderWidth: Dp = 1.dp,
		val backgroundColorAlpha: Float = 0.1f
	) : NoTagType
}