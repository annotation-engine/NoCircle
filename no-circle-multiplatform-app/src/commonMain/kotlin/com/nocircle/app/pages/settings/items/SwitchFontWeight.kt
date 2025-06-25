package com.nocircle.app.pages.settings.items

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.type.FontWeightType
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.material3.NoDropdownMenu
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun SwitchFontWeight() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			FontWeightType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							FontWeightType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_FONT_WEIGHT_TYPE.value()) },
			icon = { NoIcon(AppIcon.LineWeight.value()) },
			actions = {
				Text(
					text = FontWeightType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun FontWeightType.getAppString(): AppString = when (this) {
	FontWeightType.EXTRA_LIGHT -> AppString.SETTINGS_FONT_WEIGHT_EXTRA_LIGHT
	FontWeightType.LIGHT -> AppString.SETTINGS_FONT_WEIGHT_LIGHT
	FontWeightType.NORMAL -> AppString.SETTINGS_FONT_WEIGHT_NORMAL
	FontWeightType.BOLD -> AppString.SETTINGS_FONT_WEIGHT_BOLD
	FontWeightType.EXTRA_BOLD -> AppString.SETTINGS_FONT_WEIGHT_EXTRA_BOLD
}