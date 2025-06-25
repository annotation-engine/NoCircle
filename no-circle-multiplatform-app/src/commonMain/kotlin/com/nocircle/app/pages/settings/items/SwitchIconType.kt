package com.nocircle.app.pages.settings.items

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.material3.NoDropdownMenu
import com.nocircle.compose.resources.NoIconType
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun SwitchIconType() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			NoIconType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							NoIconType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_SWITCH_ICON_TYPE.value()) },
			icon = { NoIcon(AppIcon.ShapeLine.value()) },
			actions = {
				Text(
					text = NoIconType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun NoIconType.getAppString(): AppString = when (this) {
	NoIconType.ROUNDED -> AppString.SETTINGS_ROUNDED
	NoIconType.OUTLINED -> AppString.SETTINGS_OUTLINED
	NoIconType.FILLED -> AppString.SETTINGS_FILLED
	NoIconType.SHARP -> AppString.SETTINGS_SHARP
	NoIconType.TWO_TONE -> AppString.SETTINGS_TWO_TONE
}