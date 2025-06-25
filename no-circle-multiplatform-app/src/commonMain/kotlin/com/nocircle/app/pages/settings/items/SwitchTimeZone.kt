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
import com.nocircle.compose.resources.value
import com.nocircle.compose.time.TimeZoneId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun SwitchTimeZone() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			TimeZoneId.entries.fastForEach {
				DropdownMenuItem(
					text = {
						Text(it.offset)
					},
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							TimeZoneId.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_TIME_ZONE.value()) },
			icon = { NoIcon(AppIcon.Public.value()) },
			actions = {
				Text(
					text = TimeZoneId.current.offset,
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			},
		)
	}
}