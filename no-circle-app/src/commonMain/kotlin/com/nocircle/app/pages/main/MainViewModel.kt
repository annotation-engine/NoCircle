package com.nocircle.app.pages.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.main_home
import com.nocircle.app.generated.resources.main_message
import com.nocircle.app.generated.resources.main_person
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.StringResource

class MainViewModel : NoViewModel() {
	
	val mainRoute = MutableStateFlow(MainRoute.Home)
}

enum class MainRoute(
	val title: StringResource,
	val icon: ImageVector,
) {
	
	Home(
		title = Res.string.main_home,
		icon = Icons.Rounded.Home,
	),
	
	Message(
		title = Res.string.main_message,
		icon = Icons.AutoMirrored.Rounded.Message
	),
	
	Person(
		title = Res.string.main_person,
		icon = Icons.Rounded.Person
	)
}