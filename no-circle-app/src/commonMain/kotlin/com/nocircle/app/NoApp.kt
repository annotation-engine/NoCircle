package com.nocircle.app

import androidx.compose.runtime.Composable
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.theme.NoMaterialTheme
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val NoKoinModule = module {
	viewModel { GuideViewModel() }
	viewModel { LoginViewModel() }
	viewModel { RegisterViewModel() }
	viewModel { MainViewModel() }
	viewModel { SettingsViewModel() }
}

@Composable
fun NoApp() {
	KoinApplication(
		application = {
			modules(NoKoinModule)
		}
	) {
		NoMaterialTheme {
			NoNavHost()
		}
	}
}