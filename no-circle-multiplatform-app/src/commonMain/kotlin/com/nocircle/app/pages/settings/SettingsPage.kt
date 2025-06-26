package com.nocircle.app.pages.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.settings.items.*
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.layout.VerticalScrollColumn
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute : NoRoute

@Composable
fun SettingsPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.SETTINGS_TITLE.value()) },
				navigationIcon = if (WindowWidthSizes.isCompact) {
					{
						NoIconButton(
							icon = AppIcon.ArrowBack.value()
						) {
							controller.popBackStack()
						}
					}
				} else null
			)
		},
	) { paddingValues ->
		VerticalScrollColumn(
			modifier = Modifier.padding(
				top = paddingValues.calculateTopPadding(),
			)
		) {
			NavigateToAppearancePage()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchLanguage()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchIconType()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchShapesType()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchFontWeight()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchTimeZone()
			Spacer(modifier = Modifier.height(16.dp))
			Memory()
			Spacer(modifier = Modifier.height(16.dp))
			NavigateToAboutPage()
			Spacer(modifier = Modifier.height(16.dp))
			Logout()
		}
	}
}