package com.nocircle.compose.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtonColors
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.generated.resources.Res
import com.nocircle.compose.generated.resources.alert_modal_bottom_sheet_cancel
import com.nocircle.compose.generated.resources.alert_modal_bottom_sheet_confirm
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.resources.value
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoAlertModalBottomSheet(
	title: @Composable RowScope.() -> Unit,
	content: @Composable RowScope.() -> Unit,
	onDismissRequest: () -> Unit,
	onConfirm: suspend () -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberModalBottomSheetState(),
	confirmText: String = Res.string.alert_modal_bottom_sheet_confirm.value(),
	cancelText: String = Res.string.alert_modal_bottom_sheet_cancel.value(),
	onCancel: suspend () -> Unit = {},
	containerColor: Color = BottomSheetDefaults.ContainerColor,
	contentColor: Color = contentColorFor(containerColor),
	tonalElevation: Dp = 0.dp,
	contentWindowInsets: @Composable () -> WindowInsets = { WindowInsets(24.dp, 24.dp, 24.dp, 24.dp) },
	confirmColors: NoButtonColors = NoButtons.PrimaryColors,
	cancelColors: NoButtonColors = NoButtons.SurfaceContainerColors,
) {
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
		modifier = modifier,
		sheetState = sheetState,
		containerColor = containerColor,
		contentColor = contentColor,
		tonalElevation = tonalElevation,
		contentWindowInsets = contentWindowInsets
	) {
		CompositionLocalProvider(
			LocalTextStyle provides MaterialTheme.typography.titleLarge,
			LocalContentColor provides MaterialTheme.colorScheme.onSurface
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				title()
			}
		}
		Spacer(modifier = Modifier.height(32.dp))
		CompositionLocalProvider(
			LocalTextStyle provides MaterialTheme.typography.bodyLarge,
			LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				content()
			}
		}
		Spacer(modifier = Modifier.height(32.dp))
		val coroutineScope = rememberCoroutineScope()
		Row(
			modifier = Modifier
				.fillMaxWidth()
		) {
			NoButton(
				text = cancelText,
				modifier = Modifier
					.weight(1f),
				colors = cancelColors
			) {
				coroutineScope.launch {
					onCancel()
					sheetState.hide()
					onDismissRequest()
				}
			}
			Spacer(modifier = Modifier.width(16.dp))
			NoButton(
				text = confirmText,
				modifier = Modifier
					.weight(1f),
				colors = confirmColors
			) {
				coroutineScope.launch {
					onConfirm()
					sheetState.hide()
					onDismissRequest()
				}
			}
		}
	}
}