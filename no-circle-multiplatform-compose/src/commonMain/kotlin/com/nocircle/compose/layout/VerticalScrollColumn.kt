package com.nocircle.compose.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalScrollColumn(
	modifier: Modifier = Modifier,
	scrollState: ScrollState = rememberScrollState(),
	contentMaxWidth: Dp = 840.dp,
	contentPadding: PaddingValues = PaddingValues(16.dp, 32.dp),
	content: @Composable ColumnScope.() -> Unit
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(scrollState),
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