package com.nocircle.app

import androidx.compose.runtime.Composable
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.main.person.PersonViewModel
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
	viewModel { PersonViewModel() }
	single { SettingsViewModel() }
}

@Composable
fun NoApp(
	effectContent: @Composable (() -> Unit)? = null
) {
	KoinApplication(
		application = {
			modules(NoKoinModule)
		}
	) {
		NoMaterialTheme {
			NoAppNavHost()
			effectContent?.invoke()
		}
	}
}