package com.nocircle.compose.material3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

@Composable
fun NoDropdownMenu(
	expanded: Boolean,
	onExpandedChange: (Boolean) -> Unit,
	menuItems: @Composable ColumnScope.() -> Unit,
	shape: Shape = MaterialTheme.shapes.medium,
	content: @Composable BoxScope.() -> Unit
) {
	BoxWithConstraints(
		modifier = Modifier
			.clip(shape)
			.clickable {
				onExpandedChange(true)
			}
	) {
		var dropdownMenuWidth by remember { mutableStateOf(Dp.Hairline) }
		val density = LocalDensity.current
		val offset by remember(maxWidth, dropdownMenuWidth) {
			derivedStateOf {
				DpOffset(x = maxWidth - dropdownMenuWidth, y = Dp.Hairline)
			}
		}
		content()
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { onExpandedChange(false) },
			modifier = Modifier
				.onSizeChanged {
					dropdownMenuWidth = with(density) { it.width.toDp() }
				},
			shape = MaterialTheme.shapes.small,
			offset = offset
		) {
			menuItems()
		}
	}
}