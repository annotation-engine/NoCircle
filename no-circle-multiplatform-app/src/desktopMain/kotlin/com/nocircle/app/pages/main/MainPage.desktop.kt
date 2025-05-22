package com.nocircle.app.pages.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun LeftBarItemTooltip(
	tooltipText: String,
	isExpended: Boolean,
	content: @Composable (() -> Unit)
) {
	if (isExpended) {
		content()
	} else {
		TooltipArea(
			tooltip = {
				Text(
					text = tooltipText,
					modifier = Modifier
						.clip(MaterialTheme.shapes.small)
						.shadow(
							elevation = 8.dp,
						)
						.border(
							width = 1.dp,
							color = Color(0xFF2B2D31),
							shape = MaterialTheme.shapes.small
						)
						.background(
							color = Color(0xFF25272C),
							shape = MaterialTheme.shapes.small
						)
						.padding(
							horizontal = 12.dp,
							vertical = 8.dp
						),
					color = Color.White,
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center
				)
			},
			tooltipPlacement = TooltipPlacement.ComponentRect(
				anchor = Alignment.CenterEnd,
				alignment = Alignment.CenterEnd,
				offset = DpOffset(16.dp, 0.dp)
			)
		) {
			content()
		}
	}
}