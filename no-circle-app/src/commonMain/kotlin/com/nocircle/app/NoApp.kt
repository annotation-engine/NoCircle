package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.theme.NoMaterialTheme
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val NoKoinModule = module {
	viewModel { GuideViewModel() }
	viewModel { LoginViewModel() }
	viewModel { RegisterViewModel() }
	viewModel { MainViewModel() }
}

@Composable
fun NoApp() {
	KoinApplication(
		application = {
			modules(NoKoinModule)
		}
	) {
		NoMaterialTheme {
			val navController = rememberNavController()
			CompositionLocalProvider(
				LocalNavController provides navController
			) {
				NoNavHost()
			}
		}
	}
}

val LocalNavController = compositionLocalOf<NavHostController> { error("NoLocalNavController") }