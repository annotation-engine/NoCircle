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
import com.nocircle.compose.resources.SupportedLanguage
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun SwitchLanguage() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			SupportedLanguage.entries.fastForEach {
				DropdownMenuItem(
					text = {
						Text(it.displayName)
					},
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							SupportedLanguage.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_SWITCH_LANGUAGE.value()) },
			icon = { NoIcon(AppIcon.Language.value()) },
			actions = {
				Text(
					text = SupportedLanguage.current.displayName,
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			},
		)
	}
}
