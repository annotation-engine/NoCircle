package com.nocircle.app.pages.main.person.label

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
import com.nocircle.app.pages.main.person.PersonViewModel
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.expends.getDisplayLength
import com.nocircle.compose.expends.hexToColor
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtonColors
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.LocalSnackbarHostState
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.resources.value
import com.nocircle.shared.model.label.LabelDTO
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
				text = AppString.LABEL_TITLE.value(),
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.titleLarge
			)
		},
		icon = {
			NoIcon(
				icon = AppIcon.Edit.value(),
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
		var selectedId by remember { mutableStateOf<Int?>(null) }
		val labels by personViewModel.labels.collectAsState()
		var label by remember { mutableStateOf("") }
		var color by remember { mutableStateOf(Color.Transparent) }
		var newLabel by remember { mutableStateOf("") }
		val primary = MaterialTheme.colorScheme.primary
		var newColor by remember(primary) { mutableStateOf(primary) }
		LaunchedEffect(labels) {
			if (selectedId != null) {
				if (labels.all { it.id != selectedId }) {
					selectedId = if (labels.size >= MAX_COUNT) labels.last().id else null
				}
			} else {
				selectedId = if (labels.isNotEmpty()) labels.last().id else null
				newLabel = ""
				newColor = primary
			}
		}
		LaunchedEffect(selectedId) {
			if (selectedId != null) {
				label = labels.first { it.id == selectedId }.label
				color = hexToColor(labels.first { it.id == selectedId }.color)
			} else {
				label = newLabel
				color = newColor
			}
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
					selected = label.id == selectedId,
					onClick = { selectedId = label.id },
				)
				if (index < labels.lastIndex) {
					Spacer(modifier = Modifier.width(2.dp))
				}
			}
			val showAddLabel by remember(labels) {
				derivedStateOf { labels.size < MAX_COUNT && labels.getLabelTotalDisplayLength() < MAX_TOTAL_DISPLAY_LENGTH }
			}
			if (showAddLabel) {
				Spacer(modifier = Modifier.width(2.dp))
				AddLabel(
					selected = selectedId == null,
					onClick = { selectedId = null },
				)
			}
		}
		Spacer(modifier = Modifier.height(24.dp))
		NoTextField(
			value = label,
			onValueChange = { value ->
				val length = value.getDisplayLength()
				val total = length + labels.getLabelTotalDisplayLength { it.id != selectedId }
				if (total <= MAX_TOTAL_DISPLAY_LENGTH) {
					label = value
					if (selectedId == null) {
						newLabel = value
					}
				}
			},
			modifier = Modifier.fillMaxWidth(),
			placeholder = { Text(AppString.LABEL_PLEASE_INPUT_LABEL_NAME.value()) },
			leadingIcon = {
				NoIcon(
					icon = if (selectedId == null) AppIcon.Add.value() else AppIcon.Edit.value()
				)
			},
			suffix = {
				val total = labels.getLabelTotalDisplayLength { it.id != selectedId }
				val maxLength = MAX_TOTAL_DISPLAY_LENGTH - total
				Text("${label.getDisplayLength()} / $maxLength")
			}
		)
		Spacer(modifier = Modifier.height(24.dp))
		ColorSliders(
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
		BottomButtons(
			selectedId = selectedId,
			color = color,
			label = label,
			sheetState = sheetState,
			onDismissRequest = onDismissRequest
		)
	}
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_DISPLAY_LENGTH = 20

private fun List<LabelDTO>.getLabelTotalDisplayLength(
	predicate: ((LabelDTO) -> Boolean)? = null
): Int {
	val list = if (predicate != null) this.filter(predicate) else this
	return list.sumOf { it.label.getDisplayLength() }
}

@Composable
private fun Label(
	label: LabelDTO,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val color = hexToColor(label.color)
	Box(
		modifier = Modifier
			.fillMaxHeight()
			.clip(MaterialTheme.shapes.small)
			.clickable { onClick() }
			.border(
				width = 2.dp,
				color = if (selected) color else Color.Transparent,
				shape = MaterialTheme.shapes.small
			)
			.border(
				width = 5.dp,
				color = MaterialTheme.colorScheme.surfaceContainerLow,
				shape = MaterialTheme.shapes.small
			)
			.padding(5.dp)
			.background(color)
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
			.border(
				width = 5.dp,
				color = MaterialTheme.colorScheme.surfaceContainerLow,
				shape = MaterialTheme.shapes.small
			)
			.padding(5.dp)
			.background(MaterialTheme.colorScheme.surfaceContainerHighest),
		contentAlignment = Alignment.Center
	) {
		NoIcon(
			icon = AppIcon.Add.value(),
			tint = MaterialTheme.colorScheme.onSurface,
		)
	}
}

@Composable
private fun ColorSliders(
	color: Color,
	onColorChange: (Color) -> Unit
) {
	ColorSlider(
		title = AppString.LABEL_RED.value(),
		value = color.red * 255f,
		onValueChange = {
			val color = color.copy(red = it / 255f)
			onColorChange(color)
		},
		color = color
	)
	Spacer(modifier = Modifier.height(4.dp))
	ColorSlider(
		title = AppString.LABEL_GREEN.value(),
		value = color.green * 255f,
		onValueChange = {
			val color = color.copy(green = it / 255f)
			onColorChange(color)
		},
		color = color
	)
	Spacer(modifier = Modifier.height(4.dp))
	ColorSlider(
		title = AppString.LABEL_BLUE.value(),
		value = color.blue * 255f,
		onValueChange = {
			val color = color.copy(blue = it / 255f)
			onColorChange(color)
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
			text = AppString.LABEL_PREVIEW.value(),
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
				text = label.ifBlank { AppString.LABEL.value() },
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
private fun BottomButtons(
	selectedId: Int?,
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
		if (selectedId != null) {
			NoButton(
				text = AppString.LABEL_DELETE.value(),
				modifier = Modifier.weight(1f),
				colors = NoButtonColors.ErrorColors
			) {
				val success = viewModel.deleteLabelById(selectedId)
				if (success) {
					personViewModel.loadLabels()
				}
			}
			Spacer(modifier = Modifier.width(16.dp))
			NoButton(
				text = AppString.LABEL_UPDATE.value(),
				modifier = Modifier.weight(1f),
			) {
				val success = viewModel.updateLabel(selectedId, label, color)
				if (success) {
					personViewModel.loadLabels()
				}
			}
		} else {
			NoButton(
				text = AppString.LABEL_CANCEL.value(),
				modifier = Modifier.weight(1f),
				colors = NoButtonColors.SurfaceContainerHighColors
			) {
				sheetState.hide()
				onDismissRequest()
			}
			Spacer(modifier = Modifier.width(16.dp))
			NoButton(
				text = AppString.LABEL_ADD.value(),
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