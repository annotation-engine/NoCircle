package com.nocircle.compose.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalColumn(
	modifier: Modifier = Modifier,
	contentMaxWidth: Dp = 840.dp,
	contentPadding: PaddingValues = PaddingValues(
		start = 16.dp,
		top = 16.dp,
		end = 16.dp
	),
	content: @Composable ColumnScope.() -> Unit
) {
	Box(
		modifier = modifier
			.fillMaxSize(),
		contentAlignment = Alignment.TopCenter
	) {
		Column(
			modifier = Modifier
				.widthIn(max = contentMaxWidth)
				.fillMaxSize()
				.padding(contentPadding),
			content = content
		)
	}
}