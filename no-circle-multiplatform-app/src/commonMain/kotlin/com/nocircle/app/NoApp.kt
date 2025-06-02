package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.main.friends.FriendsViewModel
import com.nocircle.app.pages.main.person.PersonViewModel
import com.nocircle.app.pages.main.person.label.EditLabelViewModel
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.NoMaterialTheme
import com.nocircle.common.resources.LocalSupportLanguage
import com.nocircle.common.resources.SupportLanguage
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val NoKoinModule = module {
    viewModel { GuideViewModel() }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { MainViewModel() }
    viewModel { FriendsViewModel() }
    viewModel { PersonViewModel() }
    single { SettingsViewModel() }
    single { AppearanceViewModel() }
    viewModel { EditLabelViewModel() }
}

@Composable
fun NoApp(
    effect: @Composable (() -> Unit)? = null
) {
    KoinApplication(
        application = {
            modules(NoKoinModule)
        }
    ) {
        NoMaterialTheme {
            CompositionLocalAppString {
                NoAppNavHost()
            }
            effect?.invoke()
        }
    }
}


@Composable
private fun CompositionLocalAppString(
    content: @Composable () -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val language by viewModel.language.collectAsState()
    SupportLanguage.current = language
    CompositionLocalProvider(
        LocalSupportLanguage provides language,
        content = content
    )
}