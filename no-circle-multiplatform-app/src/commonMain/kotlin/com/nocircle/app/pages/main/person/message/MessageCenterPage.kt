package com.nocircle.app.pages.main.person.message

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nocircle.app.resources.AppString
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import kotlinx.serialization.Serializable

@Serializable
data object MessageCenterRoute : NoRoute

@Composable
fun MessageCenterPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.MESSAGE_CENTER_TITLE.value()) },
				onNavigationIconClick = if (WindowWidthSizes.isCompact) {
					{ controller.popBackStack() }
				} else null
			)
		}
	) {
	
	}
}