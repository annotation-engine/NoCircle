package com.nocircle.app.pages.settings.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.nocircle.app.pages.settings.about.AboutRoute
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.resources.value

@Composable
fun NavigateToAboutPage() {
	val controller = LocalNavController.current
	NoOption(
		title = { Text(AppString.ABOUT_TITLE.value()) },
		icon = { NoIcon(AppIcon.Info.value()) },
		actions = {
			Text(
				text = AppString.SETTINGS_ABOUT_SUBTITLE.value(),
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		}
	) {
		controller.navigate(AboutRoute) {
			launchSingleTop = true
		}
	}
}