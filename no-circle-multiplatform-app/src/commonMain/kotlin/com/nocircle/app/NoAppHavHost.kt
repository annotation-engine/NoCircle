package com.nocircle.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.common.expends.WindowWidthSizes
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.navigation.noComposable
import kotlinx.serialization.Serializable

@Composable
fun NoAppNavHost() {
	val navController = NoNavControllers.initAndGetRoot()
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
	data object Main {
		
		@Serializable
		data object Home
		
		@Serializable
		data object Message
		
		@Serializable
		data object Person
	}
	
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

object NoNavControllers {
	
	val root: NavHostController
		get() = _root!!
	
	private var _root by mutableStateOf<NavHostController?>(null)
	
	@Composable
	fun initAndGetRoot(): NavHostController {
		return _root ?: rememberNavController().also { _root = it }
	}
	
	private var settings by mutableStateOf<NavHostController?>(null)
	
	@Composable
	fun initAndGetSettings(): NavHostController {
		return settings ?: rememberNavController().also { settings = it }
	}
	
	@Composable
	fun auto(ifNotCompactRoute: NoRoute): NavHostController? {
		return when {
			WindowWidthSizes.isCompact -> root
			ifNotCompactRoute == NoRoutes.Main.Person -> settings
			else -> null
		}
	}
}