package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.guide.GuideRoute
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.compose.navigation.*

@Composable
fun NoAppNavHost(
	onDestinationChangedListener: NavController.OnDestinationChangedListener? = null
) {
	LocalNavControllerProvider {
		NavHost(
			navController = it,
			startDestination = GuideRoute,
			enterTransition = enterTransition { horizontalSlider() },
			exitTransition = exitTransition { horizontalSlider() },
			popEnterTransition = popEnterTransition { horizontalSlider() },
			popExitTransition = popExitTransition { horizontalSlider() },
		) {
			composable<GuideRoute> { GuidePage() }
			composable<LoginRoute> { LoginPage() }
			composable<RegisterRoute> { RegisterPage() }
			composable<MainRoute>(
				enterTransition = enterTransition { none },
				exitTransition = exitTransition { none }
			) {
				MainPage()
			}
		}
		if (onDestinationChangedListener != null) {
			DisposableEffect(Unit) {
				it.addOnDestinationChangedListener(onDestinationChangedListener)
				onDispose {
					it.removeOnDestinationChangedListener(onDestinationChangedListener)
				}
			}
		}
	}
}