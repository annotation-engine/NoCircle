package com.nocircle.compose.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalScrollColumn(
	modifier: Modifier = Modifier,
	scrollState: ScrollState = rememberScrollState(),
	overscroll: OverscrollEffect? = rememberOverscrollEffect(),
	contentMaxWidth: Dp = 840.dp,
	contentPadding: PaddingValues = PaddingValues(
		horizontal = 16.dp,
		vertical = 32.dp
	),
	verticalArrangement: Arrangement.Vertical = Arrangement.Top,
	horizontalAlignment: Alignment.Horizontal = Alignment.Start,
	content: @Composable ColumnScope.() -> Unit
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(scrollState)
			.overscroll(overscroll),
		contentAlignment = Alignment.TopCenter
	) {
		Column(
			modifier = Modifier
				.widthIn(max = contentMaxWidth)
				.fillMaxSize()
				.padding(contentPadding),
			verticalArrangement = verticalArrangement,
			horizontalAlignment = horizontalAlignment,
			content = content
		)
	}
}