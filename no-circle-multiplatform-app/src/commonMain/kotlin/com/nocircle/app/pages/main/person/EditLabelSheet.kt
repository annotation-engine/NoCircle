package com.nocircle.app.pages.main.person

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.api.LabelVO
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.getDisplayLength
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.LocalSnackbarHostState
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import com.nocircle.compose.material3.showNoSnackbar
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLabelSheet(
	onDismissRequest: () -> Unit
) {
	val sheetState = rememberNoModalBottomSheetState()
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
		sheetState = sheetState,
		title = {
			Text(
				text = AppString.LabelTitle.value(),
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.titleLarge
			)
		},
		icon = {
			NoIcon(
				icon = AppIcon.Edit.value,
				tint = MaterialTheme.colorScheme.onSurface
			)
		}
	) {
		val hostState = LocalSnackbarHostState.current
		val viewModel = koinViewModel<EditLabelViewModel>()
		val personViewModel = koinViewModel<PersonViewModel>()
		LaunchedEffect(Unit) {
			viewModel.snackbarCollect(hostState::showNoSnackbar)
		}
		var selected by remember { mutableStateOf<LabelVO?>(null) }
		val labels by personViewModel.labels.collectAsState()
		var label by remember { mutableStateOf("") }
		var addLabel by remember { mutableStateOf("") }
		val primary = MaterialTheme.colorScheme.primary
		var color by remember(primary) { mutableStateOf(primary) }
		var maxLength by remember { mutableIntStateOf(0) }
		val showAddLabel by remember(labels) {
			derivedStateOf {
				labels.getOverlength() > 0 && labels.size < MAX_COUNT
			}
		}
		LaunchedEffect(labels) {
			addLabel = ""
			val overlength = labels.getOverlength()
			selected = if (labels.size < MAX_COUNT && overlength > 0) null else labels.last()
			maxLength = overlength + if (selected != null) selected!!.label.getDisplayLength() else 0
			color = primary
			label = selected?.label ?: ""
		}
		LaunchedEffect(selected) {
			label = selected?.label ?: addLabel
			maxLength = labels.getOverlength() + if (selected != null) selected!!.label.getDisplayLength() else 0
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(48.dp)
				.horizontalScroll(
					state = rememberScrollState(),
					reverseScrolling = true
				)
		) {
			labels.fastForEachIndexed { index, label ->
				Label(
					label = label,
					selected = selected == label,
					onClick = { selected = label },
				)
				if (index < labels.lastIndex) {
					Spacer(modifier = Modifier.width(2.dp))
				}
			}
			if (showAddLabel) {
				Spacer(modifier = Modifier.width(2.dp))
				AddLabel(
					selected = selected == null,
					onClick = { selected = null },
				)
			}
		}
		Spacer(modifier = Modifier.height(24.dp))
		NoTextField(
			value = label,
			onValueChange = {
				val length = it.getDisplayLength()
				if (length <= maxLength) {
					label = it
					if (selected == null) {
						addLabel = it
					}
				}
			},
			modifier = Modifier.fillMaxWidth(),
			placeholder = { Text(AppString.LabelPleaseInputLabelName.value()) },
			leadingIcon = {
				NoIcon(
					icon = if (selected == null) AppIcon.Add.value else AppIcon.Edit.value
				)
			},
			suffix = { Text("${label.getDisplayLength()} / $maxLength") }
		)
		Spacer(modifier = Modifier.height(24.dp))
		ColorSliders(
			selected = selected,
			color = color,
			onColorChange = { color = it },
		)
		Spacer(modifier = Modifier.height(24.dp))
		LabelPreview(
			color = color,
			label = label,
			onColorChange = { color = it }
		)
		Spacer(modifier = Modifier.height(24.dp))
		ControlBottomBar(
			selected = selected,
			color = color,
			label = label,
			sheetState = sheetState,
			onDismissRequest = onDismissRequest
		)
	}
}

@Stable
private fun List<LabelVO>.getOverlength(): Int {
	return MAX_TOTAL_LENGTH - this.sumOf { it.label.getDisplayLength() }
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_LENGTH = 20

@Composable
private fun Label(
	label: LabelVO,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val color by remember(label.color) {
		derivedStateOf { Color(label.color) }
	}
	Box(
		modifier = Modifier
			.fillMaxHeight()
			.clip(MaterialTheme.shapes.small)
			.clickable { onClick() }
			.border(
				width = 2.dp,
				color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
				shape = MaterialTheme.shapes.small
			)
			.padding(5.dp)
			.background(
				color = color,
				shape = MaterialTheme.shapes.extraSmall
			)
			.padding(
				horizontal = 12.dp,
			),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = label.label,
			color = if (color.luminance() > 0.5f) Color.Black else Color.White,
			style = MaterialTheme.typography.bodyMedium,
		)
	}
}

@Composable
private fun AddLabel(
	selected: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.width(70.dp)
			.fillMaxHeight()
			.clip(MaterialTheme.shapes.small)
			.clickable { onClick() }
			.border(
				width = 2.dp,
				color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
				shape = MaterialTheme.shapes.small
			)
			.padding(5.dp)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainerHighest,
				shape = MaterialTheme.shapes.extraSmall
			),
		contentAlignment = Alignment.Center
	) {
		NoIcon(
			icon = AppIcon.Add.value,
			tint = MaterialTheme.colorScheme.onSurface,
		)
	}
}

@Composable
private fun ColorSliders(
	selected: LabelVO?,
	color: Color,
	onColorChange: (Color) -> Unit
) {
	val primary = MaterialTheme.colorScheme.primary
	var addColor by remember(primary) { mutableStateOf(primary) }
	LaunchedEffect(selected) {
		onColorChange(selected?.color?.let { Color(it) } ?: addColor)
	}
	ColorSlider(
		title = AppString.LabelRed.value(),
		value = color.red * 255f,
		onValueChange = {
			val color = color.copy(red = it / 255f)
			onColorChange(color)
			if (selected == null) {
				addColor = color
			}
		},
		color = color
	)
	Spacer(modifier = Modifier.height(4.dp))
	ColorSlider(
		title = AppString.LabelGreen.value(),
		value = color.green * 255f,
		onValueChange = {
			val color = color.copy(green = it / 255f)
			onColorChange(color)
			if (selected == null) {
				addColor = color
			}
		},
		color = color
	)
	Spacer(modifier = Modifier.height(4.dp))
	ColorSlider(
		title = AppString.LabelBlue.value(),
		value = color.blue * 255f,
		onValueChange = {
			val color = color.copy(blue = it / 255f)
			onColorChange(color)
			if (selected == null) {
				addColor = color
			}
		},
		color = color
	)
}

@Composable
private fun ColorSlider(
	title: String,
	value: Float,
	onValueChange: (Float) -> Unit,
	color: Color,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = title,
			modifier = Modifier.width(50.dp),
			textAlign = TextAlign.End,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.bodyMedium,
			maxLines = 1
		)
		Spacer(modifier = Modifier.width(12.dp))
		Slider(
			value = value,
			onValueChange = { onValueChange(it) },
			modifier = Modifier.weight(1f),
			valueRange = 0f..255f,
			colors = SliderDefaults.colors(
				thumbColor = color,
				activeTrackColor = color,
			)
		)
		Spacer(modifier = Modifier.width(12.dp))
		Text(
			text = value.roundToInt().toString(),
			modifier = Modifier.width(28.dp),
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.bodyMedium,
			textAlign = TextAlign.End,
		)
	}
}

@Composable
private fun LabelPreview(
	color: Color,
	label: String,
	onColorChange: (Color) -> Unit,
) {
	Row(
		modifier = Modifier
			.height(38.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = AppString.LabelPreview.value(),
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.bodyMedium,
		)
		Spacer(modifier = Modifier.width(12.dp))
		Box(
			modifier = Modifier
				.clip(MaterialTheme.shapes.extraSmall)
				.fillMaxHeight()
				.background(
					color = color,
					shape = MaterialTheme.shapes.extraSmall
				)
				.padding(
					horizontal = 12.dp
				),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = label.ifBlank { AppString.Label.value() },
				color = if (color.luminance() > 0.5f) Color.Black else Color.White,
				style = MaterialTheme.typography.bodyMedium,
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		Spacer(modifier = Modifier.width(12.dp))
		val colors = arrayOf(
			MaterialTheme.colorScheme.primary,
			MaterialTheme.colorScheme.secondary,
			MaterialTheme.colorScheme.tertiary
		)
		colors.forEachIndexed { index, color ->
			Box(
				modifier = Modifier
					.size(38.dp)
					.clip(MaterialTheme.shapes.extraSmall)
					.background(
						color = color,
						shape = MaterialTheme.shapes.extraSmall
					)
					.clickable {
						onColorChange(color)
					}
			)
			if (index < colors.lastIndex) {
				Spacer(modifier = Modifier.width(6.dp))
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ControlBottomBar(
	selected: LabelVO?,
	color: Color,
	label: String,
	sheetState: SheetState,
	onDismissRequest: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		val viewModel = koinViewModel<EditLabelViewModel>()
		val personViewModel = koinViewModel<PersonViewModel>()
		if (selected != null) {
			NoButton(
				text = AppString.LabelDelete.value(),
				modifier = Modifier.weight(1f),
				colors = NoButtons.ErrorColors
			) {
				val success = viewModel.deleteLabelById(selected.id)
				if (success) {
					personViewModel.loadLabels()
				}
			}
			Spacer(modifier = Modifier.width(16.dp))
			NoButton(
				text = AppString.LabelUpdate.value(),
				modifier = Modifier.weight(1f),
			) {
				val success = viewModel.updateLabel(selected.id, label, color)
				if (success) {
					personViewModel.loadLabels()
				}
			}
		} else {
			NoButton(
				text = AppString.LabelCancel.value(),
				modifier = Modifier.weight(1f),
				colors = NoButtons.SurfaceContainerColors
			) {
				sheetState.hide()
				onDismissRequest()
			}
			Spacer(modifier = Modifier.width(16.dp))
			NoButton(
				text = AppString.LabelAdd.value(),
				modifier = Modifier.weight(1f)
			) {
				val success = viewModel.addLabel(label, color)
				if (success) {
					personViewModel.loadLabels()
				}
			}
		}
	}
}