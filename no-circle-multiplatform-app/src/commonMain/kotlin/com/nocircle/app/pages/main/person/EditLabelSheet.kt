package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.api.LabelVO
import com.nocircle.common.expends.hexToColor
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoModalBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLabelSheet(
	onDismissRequest: () -> Unit
) {
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(220.dp)
		) {
			val viewModel = koinViewModel<PersonViewModel>()
			
			Column(
				modifier = Modifier
					.width(140.dp)
					.fillMaxHeight()
			) {
				val labels by viewModel.labels.collectAsState()
				var selectedIndex by remember { mutableStateOf(0) }
				labels?.let {
					it.fastForEachIndexed { index, label ->
						Label(
							label = label,
							selected = selectedIndex == index,
							onClick = { selectedIndex = index }
						)
					}
					if (it.size < 5) {
						AddLabel(
							onClick = { selectedIndex = -1 },
						)
					}
				}
			}
		}
	}
}

@Composable
private fun Label(
	label: LabelVO,
	selected: Boolean,
	onClick: () -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(44.dp)
			.clip(MaterialTheme.shapes.small)
			.clickable(onClick = onClick)
			.then(
				if (!selected) Modifier else Modifier.border(
					width = 2.dp,
					shape = MaterialTheme.shapes.small,
					color = MaterialTheme.colorScheme.primary,
				)
			)
			.padding(4.dp)
			.clip(MaterialTheme.shapes.extraSmall)
			.background(label.color.hexToColor())
			.padding(horizontal = 12.dp),
		contentAlignment = Alignment.CenterStart
	) {
		Text(
			text = label.label,
			color = Color.White,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}

@Composable
private fun AddLabel(
	onClick: () -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(44.dp)
			.padding(4.dp)
			.clip(MaterialTheme.shapes.extraSmall)
			.background(
				color = MaterialTheme.colorScheme.surfaceDim
			)
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center
	) {
		NoIcon(
			icon = Icons.Rounded.Add,
			tint = MaterialTheme.colorScheme.primary,
		)
	}
}