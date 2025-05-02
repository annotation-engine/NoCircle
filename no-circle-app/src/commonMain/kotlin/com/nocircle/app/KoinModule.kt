package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.main.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


private val koinModule = module {
	viewModel { LoginViewModel() }
	viewModel { RegisterViewModel() }
	viewModel { MainViewModel() }
}

@Composable
fun KoinModules() {
	LaunchedEffect(Unit) {
		startKoin {
			modules(koinModule)
		}
	}
}