package com.nocircle.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.common.expends.noComposable
import kotlinx.serialization.Serializable

@Composable
fun NoNavHost() {
	val navController = LocalNavController.current
	NavHost(
		navController = navController,
		startDestination = NoRoutes.Guide,
		enterTransition = {
			slideInHorizontally(
				initialOffsetX = { it }, // 从右进来
				animationSpec = tween(300)
			)
		},
		exitTransition = {
			slideOutHorizontally(
				targetOffsetX = { -it }, // 向左退出
				animationSpec = tween(300)
			)
		},
		popEnterTransition = {
			slideInHorizontally(
				initialOffsetX = { -it }, // 从左进来（回退）
				animationSpec = tween(300)
			)
		},
		popExitTransition = {
			slideOutHorizontally(
				targetOffsetX = { it }, // 向右退出（回退）
				animationSpec = tween(300)
			)
		}
	) {
		noComposable<NoRoutes.Guide> {
			GuidePage()
		}
		
		noComposable<NoRoutes.AccountLogin> {
			LoginPage()
		}
		
		noComposable<NoRoutes.AccountRegister> {
			RegisterPage()
		}
		
		noComposable<NoRoutes.Main> {
			MainPage()
		}
	}
}

object NoRoutes {
	
	@Serializable
	data object Guide
	
	@Serializable
	data object AccountLogin
	
	@Serializable
	data object AccountRegister
	
	@Serializable
	data object Main
	
	@Serializable
	data object Settings
}