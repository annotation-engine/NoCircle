package com.nocircle.compose.material3

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoModalBottomSheet(
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberModalBottomSheetState(),
	containerColor: Color = BottomSheetDefaults.ContainerColor,
	contentColor: Color = contentColorFor(containerColor),
	tonalElevation: Dp = 0.dp,
	contentWindowInsets: @Composable () -> WindowInsets = { WindowInsets(24.dp, 24.dp, 24.dp, 24.dp) },
	content: @Composable ColumnScope.() -> Unit
) {
	ModalBottomSheet(
		onDismissRequest = onDismissRequest,
		modifier = modifier
			.padding(12.dp),
		sheetState = sheetState,
		containerColor = containerColor,
		contentColor = contentColor,
		shape = MaterialTheme.shapes.extraLarge,
		tonalElevation = tonalElevation,
		dragHandle = {},
		contentWindowInsets = contentWindowInsets,
		content = content,
	)
}