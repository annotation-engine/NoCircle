package com.nocircle.app.pages.settings.items

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.type.RoundedCornerType
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.material3.NoDropdownMenu
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun SwitchShapesType() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			RoundedCornerType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							RoundedCornerType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_ROUNDED_CORNER_TYPE.value()) },
			icon = { NoIcon(AppIcon.RoundedCorner.value()) },
			actions = {
				Text(
					text = RoundedCornerType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun RoundedCornerType.getAppString(): AppString = when (this) {
	RoundedCornerType.EXTRA_SMALL -> AppString.SETTINGS_ROUNDED_CORNER_EXTRA_SMALL
	RoundedCornerType.SMALL -> AppString.SETTINGS_ROUNDED_CORNER_SMALL
	RoundedCornerType.NORMAL -> AppString.SETTINGS_ROUNDED_CORNER_NORMAL
	RoundedCornerType.LARGE -> AppString.SETTINGS_ROUNDED_CORNER_LARGE
	RoundedCornerType.EXTRA_LARGE -> AppString.SETTINGS_ROUNDED_CORNER_EXTRA_LARGE
}