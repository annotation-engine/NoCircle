package com.nocircle.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.common.expends.noComposable
import kotlinx.serialization.Serializable

@Composable
fun NoNavHost() {
	val navController = LocalNavController.current
	NavHost(
		navController = navController,
		startDestination = NoRoutes.Guide,
		enterTransition = { EnterTransition },
		exitTransition = { ExitTransition },
		popEnterTransition = { PopEnterTransition },
		popExitTransition = { PopExitTransition }
	) {
		noComposable<NoRoutes.Guide> { GuidePage() }
		noComposable<NoRoutes.Login> { LoginPage() }
		noComposable<NoRoutes.Register> { RegisterPage() }
		noComposable<NoRoutes.Main> { MainPage() }
		noComposable<NoRoutes.Settings> { SettingsPage() }
	}
}

object NoRoutes {
	
	@Serializable
	data object Guide
	
	@Serializable
	data object Login
	
	@Serializable
	data object Register
	
	@Serializable
	data object Main
	
	@Serializable
	data object Settings
}

private val EnterTransition = slideInHorizontally(
	initialOffsetX = { it },
	animationSpec = tween(300)
)

private val ExitTransition = slideOutHorizontally(
	targetOffsetX = { -it },
	animationSpec = tween(300)
)

private val PopEnterTransition = slideInHorizontally(
	initialOffsetX = { -it },
	animationSpec = tween(300)
)

private val PopExitTransition = slideOutHorizontally(
	targetOffsetX = { it },
	animationSpec = tween(300)
)

@Composable
fun NavControllerProvider(
	content: @Composable () -> Unit
) {
	val navController = rememberNavController()
	CompositionLocalProvider(
		LocalNavController provides navController,
		content = content
	)
}

val LocalNavController = compositionLocalOf<NavHostController> { error("NoLocalNavController") }