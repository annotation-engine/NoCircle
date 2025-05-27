package com.nocircle.app.pages.main.person

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
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
import com.nocircle.common.expends.getDisplayLength
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.expends.rememberHexToColor
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoInput
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.rememberNoModalBottomSheetState
import org.koin.compose.viewmodel.koinViewModel

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
				text = "编辑个人标签",
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.titleLarge
			)
		},
		icon = {
			NoIcon(
				icon = Icons.Rounded.Edit,
				tint = MaterialTheme.colorScheme.onSurface
			)
		}
	) {
		Column(
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.widthIn(max = 450.dp)
				.fillMaxWidth()
		) {
			val viewModel = koinViewModel<PersonViewModel>()
			var selected by remember { mutableStateOf<LabelVO?>(null) }
			val labels by viewModel.labels.collectAsState()
			val currentLabels = remember { mutableStateListOf<LabelVO>() }
			LaunchedEffect(labels) {
				if (labels == null) return@LaunchedEffect
				currentLabels.clear()
				currentLabels += labels!!
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
				currentLabels.fastForEachIndexed { index, label ->
					Label(
						label = label,
						selected = selected == label,
						onClick = { selected = label },
					)
					if (index < currentLabels.lastIndex) {
						Spacer(modifier = Modifier.width(2.dp))
					}
				}
				if (currentLabels.size < 5) {
					Spacer(modifier = Modifier.width(2.dp))
					AddLabel(
						selected = selected == null,
						onClick = { selected = null },
					)
				}
			}
			Spacer(modifier = Modifier.height(24.dp))
			var label by remember { mutableStateOf("") }
			var addLabel by remember { mutableStateOf("") }
			LaunchedEffect(selected) {
				label = selected?.label ?: addLabel
			}
			NoInput(
				value = label,
				onValueChange = {
					val length = it.getDisplayLength()
					if (length <= 10) {
						label = it
						if (selected == null) {
							addLabel = it
						}
					}
				},
				placeholder = "请输入标签名称",
				suffix = { Text("${label.getDisplayLength()} / 10") }
			)
			Spacer(modifier = Modifier.height(24.dp))
			var color by remember { mutableStateOf(Color.Transparent) }
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
}

@Composable
private fun Label(
	label: LabelVO,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val containerColor = rememberHexToColor(label.color)
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
				color = containerColor,
				shape = MaterialTheme.shapes.extraSmall
			)
			.padding(
				horizontal = 12.dp,
			),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = label.label,
			color = if (containerColor.luminance() > 0.5f) Color.Black else Color.White,
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
			icon = Icons.Rounded.Add,
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
		onColorChange(selected?.color?.hexToColor() ?: addColor)
	}
	ColorSlider(
		title = "红",
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
	ColorSlider(
		title = "绿",
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
	ColorSlider(
		title = "蓝",
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
	color: Color
) {
	Row(
		modifier = Modifier
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.bodyMedium,
		)
		Spacer(modifier = Modifier.width(12.dp))
		Slider(
			value = value,
			onValueChange = { onValueChange(it) },
			modifier = Modifier.weight(1f),
			valueRange = 0f .. 255f,
			colors = SliderDefaults.colors(
				thumbColor = color,
				activeTrackColor = color,
			)
		)
		Spacer(modifier = Modifier.width(12.dp))
		Text(
			text = value.toInt().toString(),
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
			text = "预览",
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
				text = label.ifBlank { "标签" },
				color = if (color.luminance() > 0.5f) Color.Black else Color.White,
				style = MaterialTheme.typography.bodyMedium,
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		Text(
			text = "预设",
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.bodyMedium,
		)
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
		val viewModel = koinViewModel<PersonViewModel>()
		NoButton(
			text = if (selected != null) "删除" else "取消",
			modifier = Modifier
				.weight(1f),
			colors = if (selected != null) NoButtons.ErrorColors else NoButtons.SurfaceContainerColors
		) {
			if (selected != null) {
				val success = viewModel.deleteLabelById(selected.id)
				if (success) {
					sheetState.hide()
					onDismissRequest()
				}
			} else {
				sheetState.hide()
				onDismissRequest()
			}
		}
		Spacer(modifier = Modifier.width(16.dp))
		NoButton(
			text = if (selected != null) "修改" else "添加",
			modifier = Modifier
				.weight(1f),
		) {
			val success = viewModel.addLabel(label, color)
			if (success) {
				sheetState.hide()
				onDismissRequest()
			}
		}
	}
}