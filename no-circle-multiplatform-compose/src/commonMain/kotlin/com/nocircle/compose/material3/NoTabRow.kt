package com.nocircle.compose.material3

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun <T> NoTabRow(
	selected: T,
	onSelectedChange: (T) -> Unit,
	items: List<T>,
	modifier: Modifier = Modifier,
	shape: Shape = NoTabRowDefaults.shape,
	interval: Dp = NoTabRowDefaults.interval,
	sliderColor: Color = MaterialTheme.colorScheme.primary,
	selectedContentColor: Color = contentColorFor(sliderColor),
	unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
	disabledTabs: List<T> = emptyList(),
	tab: @Composable RowScope.(item: T) -> Unit
) {
	BoxWithConstraints(
		modifier = modifier
			.fillMaxWidth()
			.height(52.dp)
			.clip(shape),
	) {
		val tabWidth by remember(maxWidth, interval, items.size) {
			derivedStateOf { (maxWidth - interval * (items.size - 1)) / items.size }
		}
		val selectedIndex by remember(items, selected) {
			derivedStateOf {
				val index = items.indexOf(selected)
				if (index != -1) index else 0
			}
		}
		val offsetX by animateDpAsState(
			targetValue = (tabWidth + interval) * selectedIndex
		)
		Box(
			modifier = Modifier
				.offset(offsetX)
				.width(tabWidth)
				.fillMaxHeight()
				.clip(shape)
				.background(
					color = sliderColor,
					shape = shape
				)
		)
		Row(
			modifier = Modifier
				.fillMaxSize()
		) {
			items.fastForEachIndexed { index, item ->
				val enabled by remember(item, disabledTabs) {
					derivedStateOf { item !in disabledTabs }
				}
				Row(
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
						.clip(shape)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = LocalIndication.current,
							enabled = enabled,
							onClick = { onSelectedChange(item) }
						),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					CompositionLocalProvider(
						LocalContentColor provides if (selected == item) selectedContentColor else unselectedContentColor,
					) {
						tab(item)
					}
				}
				if (index < items.lastIndex) {
					Spacer(modifier = Modifier.width(interval))
				}
			}
		}
	}
}

object NoTabRowDefaults {
	
	val interval = 8.dp
	
	val shape: Shape
		@Composable
		get() = MaterialTheme.shapes.medium
}