package com.nocircle.app

import androidx.compose.runtime.Composable
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.guide.GuideRoute
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.common.navigation.NoNavControllerManager
import com.nocircle.common.navigation.NoNavHost

@Composable
fun NoAppNavHost() {
	val navController = NoNavControllerManager.get()
	NoNavHost(
		navController = navController,
		startDestination = GuideRoute
	) {
		composable<GuideRoute> { GuidePage() }
		composable<LoginRoute> { LoginPage() }
		composable<RegisterRoute> { RegisterPage() }
		composable<MainRoute> { MainPage() }
		composable<SettingsRoute> { SettingsPage() }
		composable<AppearanceRoute> { AppearancePage() }
	}
}