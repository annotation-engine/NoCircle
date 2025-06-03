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
import com.nocircle.app.pages.main.friends.list.AddFriendSheetViewModel
import com.nocircle.app.pages.main.friends.list.FriendsListViewModel
import com.nocircle.app.pages.main.person.EditLabelViewModel
import com.nocircle.app.pages.main.person.PersonViewModel
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.NoMaterialTheme
import com.nocircle.common.resources.IconType
import com.nocircle.common.resources.LocalIconType
import com.nocircle.common.resources.LocalSupportLanguage
import com.nocircle.common.resources.SupportLanguage
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val NoKoinModule = module {
	viewModel { GuideViewModel() }
	viewModel { LoginViewModel() }
	viewModel { RegisterViewModel() }
	viewModel { MainViewModel() }
	viewModel { FriendsViewModel() }
	viewModel { FriendsListViewModel() }
	viewModel { AddFriendSheetViewModel() }
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
			CompositionLocalConfig {
				NoAppNavHost()
			}
			effect?.invoke()
		}
	}
}

@Composable
private fun CompositionLocalConfig(
	content: @Composable () -> Unit
) {
	val supportLanguage by SupportLanguage.current.collectAsState()
	val iconType by IconType.current.collectAsState()
	CompositionLocalProvider(
		LocalSupportLanguage provides supportLanguage,
		LocalIconType provides iconType,
		content = content
	)
}