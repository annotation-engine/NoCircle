package com.nocircle.app

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.main.friends.FriendsViewModel
import com.nocircle.app.pages.main.friends.detail.FriendDetailViewModel
import com.nocircle.app.pages.main.friends.list.FriendListViewModel
import com.nocircle.app.pages.main.friends.list.add.AddFriendViewModel
import com.nocircle.app.pages.main.person.PersonViewModel
import com.nocircle.app.pages.main.person.label.EditLabelViewModel
import com.nocircle.app.pages.main.person.message.MessageCenterViewModel
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.pages.settings.about.AboutViewModel
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.NoMaterialTheme
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val NoKoinModule = module {
	viewModel { GuideViewModel() }
	viewModel { LoginViewModel() }
	viewModel { RegisterViewModel() }
	viewModel { MainViewModel() }
	viewModel { FriendsViewModel() }
	viewModel { FriendListViewModel() }
	viewModel { FriendDetailViewModel() }
	viewModel { AddFriendViewModel() }
	viewModel { PersonViewModel() }
	viewModel { SettingsViewModel() }
	viewModel { AppearanceViewModel() }
	viewModel { EditLabelViewModel() }
	viewModel { MessageCenterViewModel() }
	viewModel { AboutViewModel() }
}

@Composable
fun NoApp(
	onDestinationChangedListener: NavController.OnDestinationChangedListener? = null,
	onColorSchemeChange: ((ColorScheme) -> Unit)? = null
) {
	KoinApplication(
		application = {
			modules(NoKoinModule)
		}
	) {
		NoMaterialTheme {
			NoAppNavHost(onDestinationChangedListener)
			if (onColorSchemeChange != null) {
				val colorScheme = MaterialTheme.colorScheme
				LaunchedEffect(colorScheme) {
					onColorSchemeChange(colorScheme)
				}
			}
		}
	}
}