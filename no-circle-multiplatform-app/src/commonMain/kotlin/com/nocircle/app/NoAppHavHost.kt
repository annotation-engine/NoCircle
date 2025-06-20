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
import com.nocircle.compose.navigation.HorizontalSlideTransition
import com.nocircle.compose.navigation.LocalNavControllerProvider
import com.nocircle.compose.navigation.NoneTransition

@Composable
fun NoAppNavHost(
	onDestinationChangedListener: NavController.OnDestinationChangedListener? = null
) {
	LocalNavControllerProvider {
		NavHost(
			navController = it,
			startDestination = GuideRoute,
			enterTransition = HorizontalSlideTransition.Enter,
			exitTransition = HorizontalSlideTransition.Exit,
			popEnterTransition = HorizontalSlideTransition.PopEnter,
			popExitTransition = HorizontalSlideTransition.PopExit,
		) {
			composable<GuideRoute> { GuidePage() }
			composable<LoginRoute> { LoginPage() }
			composable<RegisterRoute> { RegisterPage() }
			composable<MainRoute>(
				enterTransition = NoneTransition.Enter,
				exitTransition = NoneTransition.Exit
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