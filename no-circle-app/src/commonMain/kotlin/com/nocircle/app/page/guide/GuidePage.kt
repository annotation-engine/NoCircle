package com.nocircle.app.page.guide

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.nocircle.app.NoRoute
import com.nocircle.app.database.ConfigDao
import com.nocircle.common.expends.navigate
import com.nocircle.compose.material3.NoScaffold
import kotlinx.coroutines.delay

@Composable
fun GuidePage(
	navController: NavController
) {
	LaunchedEffect(Unit) {
		delay(3000L)
		val token = ConfigDao.getValue<String>("token")
		if (token != null) {
			navController.navigate(route = NoRoute.MAIN, finish = true)
		} else {
			navController.navigate(route = NoRoute.ACCOUNT_LOGIN, finish = true)
		}
	}
	NoScaffold {
	
	}
}