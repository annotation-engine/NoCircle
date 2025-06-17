package com.nocircle.compose.material3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.compose.resources.value
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.resources.ComposeIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoModalBottomSheet(
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberNoModalBottomSheetState(),
	snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
	containerColor: Color = BottomSheetDefaults.ContainerColor,
	contentColor: Color = contentColorFor(containerColor),
	tonalElevation: Dp = 0.dp,
	contentWindowInsets: @Composable () -> WindowInsets = NoModalBottomSheetDefaults.ContentWindowInsets,
	icon: @Composable (() -> Unit)? = null,
	title: @Composable (() -> Unit)? = null,
	showCloseButton: Boolean = true,
	contentMaxWidth: Dp = NoModalBottomSheetDefaults.ContentMaxWidth,
	content: @Composable ColumnScope.() -> Unit
) {
	ModalBottomSheet(
		onDismissRequest = onDismissRequest,
		modifier = modifier
			.padding(16.dp),
		sheetState = sheetState,
		containerColor = containerColor,
		contentColor = contentColor,
		shape = MaterialTheme.shapes.medium,
		tonalElevation = tonalElevation,
		dragHandle = {},
		contentWindowInsets = contentWindowInsets
	) {
		Box {
			Column(
				modifier = Modifier
					.fillMaxWidth()
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
								NoIconButton(
									icon = ComposeIcon.Close.value(),
									tint = MaterialTheme.colorScheme.onSurface
								) {
									sheetState.hide()
									onDismissRequest()
								}
							}
						}
						Spacer(modifier = Modifier.height(32.dp))
					}
				}
				Column(
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.widthIn(max = contentMaxWidth)
						.fillMaxWidth()
				) {
					CompositionLocalProvider(
						LocalSnackbarHostState provides snackbarHostState
					) {
						content()
					}
				}
			}
			NoSnackbarHost(
				hostState = snackbarHostState,
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.offset(y = 10.dp)
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberNoModalBottomSheetState() = rememberModalBottomSheetState(
	skipPartiallyExpanded = true
)

object NoModalBottomSheetDefaults {
	
	val ContentMaxWidth = 450.dp
	
	val ContentWindowInsets = @Composable { WindowInsets(24.dp, 24.dp, 24.dp, 24.dp) }
}