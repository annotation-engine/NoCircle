package com.nocircle.app

import androidx.compose.runtime.Composable
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.common.navigation.NoNavHost
import com.nocircle.common.navigation.NoNavHostController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.navigation.rememberNoNavController
import com.nocircle.common.windowsize.WindowWidthSizes
import kotlinx.serialization.Serializable

@Composable
fun NoAppNavHost() {
	val navController = NoNavControllerManagers.get()
	NoNavHost(
		navController = navController,
		startDestination = NoRoutes.Guide
	) {
		composable<NoRoutes.Guide> { GuidePage() }
		composable<NoRoutes.Login> { LoginPage() }
		composable<NoRoutes.Register> { RegisterPage() }
		composable<NoRoutes.Main> { MainPage() }
		composable<NoRoutes.Settings> { SettingsPage() }
		composable<NoRoutes.Settings.Appearance> { AppearancePage() }
	}
}

object NoRoutes {
	
	@Serializable
	data object Guide : NoRoute
	
	@Serializable
	data object Login : NoRoute
	
	@Serializable
	data object Register : NoRoute
	
	@Serializable
	data object Main : NoRoute {
		
		@Serializable
		data object Home : NoRoute
		
		@Serializable
		data object Message : NoRoute
		
		@Serializable
		data object Person : NoRoute
	}
	
	@Serializable
	data object Settings : NoRoute {
		
		@Serializable
		data object Appearance : NoRoute
	}
}

object NoNavControllerManagers {
	
	private val controllers = mutableMapOf<NoNavHost, NoNavHostController>()
	
	@Composable
	fun get(
		navHost: NoNavHost = NoNavHost.Root,
		moreNavHost: NoNavHost = navHost
	): NoNavHostController {
		val navHost = if (WindowWidthSizes.isCompact) navHost else moreNavHost
		return controllers.getOrPut(navHost) {
			rememberNoNavController()
		}
	}
}

enum class NoNavHost {
	Root,
	Person,
	Other
}