package com.nocircle.app.pages.main.person.request

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import kotlinx.serialization.Serializable

@Serializable
data object FriendAddRequestRoute : NoRoute

@Composable
fun FriendAddRequestPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text("好友请求消息") },
				onNavigationIconClick = if (WindowWidthSizes.isCompact) {
					{ controller.popBackStack() }
				} else null
			)
		}
	) {
	
	}
}