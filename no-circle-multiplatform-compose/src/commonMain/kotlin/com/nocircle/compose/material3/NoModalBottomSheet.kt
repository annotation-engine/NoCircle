package com.nocircle.compose.material3

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.compose.foundation.NoIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoModalBottomSheet(
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberNoModalBottomSheetState(),
	containerColor: Color = BottomSheetDefaults.ContainerColor,
	contentColor: Color = contentColorFor(containerColor),
	tonalElevation: Dp = 0.dp,
	contentWindowInsets: @Composable () -> WindowInsets = { WindowInsets(24.dp, 24.dp, 24.dp, 24.dp) },
	icon: @Composable (() -> Unit)? = null,
	title: @Composable (() -> Unit)? = null,
	showCloseButton: Boolean = false,
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
		contentWindowInsets = contentWindowInsets
	) {
		if (icon != null || title != null || showCloseButton) {
			CompositionLocalProvider(
				LocalTextStyle provides MaterialTheme.typography.titleLarge,
				LocalContentColor provides MaterialTheme.colorScheme.onSurface
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					if (icon != null) {
						icon()
						Spacer(modifier = Modifier.width(8.dp))
					}
					if (title != null) {
						title()
					}
					Spacer(modifier = Modifier.weight(1f))
					if (showCloseButton) {
						NoIcon(
							icon = Icons.Rounded.Close,
							tint = MaterialTheme.colorScheme.onSurface
						)
					}
				}
				Spacer(modifier = Modifier.height(32.dp))
			}
		}
		content()
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberNoModalBottomSheetState() = rememberModalBottomSheetState(
	skipPartiallyExpanded = true
)