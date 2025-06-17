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
import com.nocircle.compose.navigation.LocalNavControllerProvider
import com.nocircle.compose.navigation.NavPopTransition
import com.nocircle.compose.navigation.NavTransition
import com.nocircle.compose.navigation.NoNavHost
import com.nocircle.compose.navigation.NoNavHostController
import com.nocircle.compose.navigation.composable

var rootController: NoNavHostController? = null

@Composable
fun NoAppNavHost() {
	LocalNavControllerProvider {
		rootController = it
		NoNavHost(
			navController = it,
			startDestination = GuideRoute
		) {
			composable<GuideRoute> { GuidePage() }
			composable<LoginRoute> { LoginPage() }
			composable<RegisterRoute> { RegisterPage() }
			composable<MainRoute>(
				navTransition = NavTransition.None,
				navPopTransition = NavPopTransition.None,
				content = { MainPage() }
			)
		}
	}
}