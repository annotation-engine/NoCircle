package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.common.navigation.NoNavHost
import com.nocircle.common.navigation.NoNavHostController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.navigation.rememberNoNavController
import kotlinx.serialization.Serializable

@Composable
fun NoAppNavHost() {
	val navController = NoNavControllerManagers.initAndGetRoot()
	NoNavHost(
		navController = navController,
		startDestination = NoRoutes.Guide
	) {
		composable<NoRoutes.Guide> { GuidePage() }
		composable<NoRoutes.Login> { LoginPage() }
		composable<NoRoutes.Register> { RegisterPage() }
		composable<NoRoutes.Main> { MainPage() }
		composable<NoRoutes.Settings> { SettingsPage() }
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
	data object Settings : NoRoute
}

object NoNavControllerManagers {
	
	val root: NoNavHostController
		get() = _root!!
	
	private var _root by mutableStateOf<NoNavHostController?>(null)
	
	@Composable
	fun initAndGetRoot(): NoNavHostController {
		return _root ?: rememberNoNavController().also { _root = it }
	}
	
	private var settings by mutableStateOf<NoNavHostController?>(null)
	
	@Composable
	fun initAndGetSettings(): NoNavHostController {
		return settings ?: rememberNoNavController().also { settings = it }
	}
	
	@Composable
	fun auto(ifNotCompactRoute: NoRoute): NoNavHostController? {
		return when {
			WindowWidthSizes.isCompact -> root
			ifNotCompactRoute == NoRoutes.Main.Person -> settings
			else -> null
		}
	}
}