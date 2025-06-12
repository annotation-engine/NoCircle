package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nocircle.app.pages.account.login.LoginViewModel
import com.nocircle.app.pages.account.register.RegisterViewModel
import com.nocircle.app.pages.guide.GuideViewModel
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.main.friends.FriendsViewModel
import com.nocircle.app.pages.main.friends.list.FriendsListViewModel
import com.nocircle.app.pages.main.friends.list.add.AddFriendViewModel
import com.nocircle.app.pages.main.person.PersonViewModel
import com.nocircle.app.pages.main.person.label.EditLabelViewModel
import com.nocircle.app.pages.main.person.message.MessageCenterViewModel
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.NoMaterialTheme
import com.nocircle.common.resources.loadStringJsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.compose.KoinApplication
import org.koin.dsl.module

private val NoKoinModule = module {
	single { GuideViewModel() }
	single { LoginViewModel() }
	single { RegisterViewModel() }
	single { MainViewModel() }
	single { FriendsViewModel() }
	single { FriendsListViewModel() }
	single { AddFriendViewModel() }
	single { PersonViewModel() }
	single { SettingsViewModel() }
	single { AppearanceViewModel() }
	single { EditLabelViewModel() }
	single { MessageCenterViewModel() }
}

@Composable
fun NoApp(
	effect: @Composable (() -> Unit)? = null
) {
	PreloadAllString()
	KoinApplication(
		application = {
			modules(NoKoinModule)
		}
	) {
		NoMaterialTheme {
			NoAppNavHost()
			effect?.invoke()
		}
	}
}

@Composable
private fun PreloadAllString() {
	LaunchedEffect(Unit) {
		withContext(Dispatchers.IO) {
			loadStringJsonObject("com.nocircle.app", listOf("strings.json"))
			loadStringJsonObject("com.nocircle.compose", listOf("strings.json"))
		}
	}
}