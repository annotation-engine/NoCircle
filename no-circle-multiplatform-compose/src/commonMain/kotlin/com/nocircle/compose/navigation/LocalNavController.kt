package com.nocircle.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nocircle.compose.expends.noLocalProvidedFor

val LocalNavController = staticCompositionLocalOf<NavHostController> {
	noLocalProvidedFor("LocalNavController")
}

private val navControllerMap = mutableMapOf<Any, NavHostController>()

fun getNavController(key: Any = Unit): NavHostController =
	navControllerMap[key]!!

fun getNavControllerOrNull(key: Any = Unit): NavHostController? =
	navControllerMap[key]

@Composable
fun LocalNavControllerProvider(
	key: Any = Unit,
	content: @Composable (NavHostController) -> Unit
) {
	CompositionLocalProvider(
		LocalNavController provides rememberNavController()
	) {
		val current = LocalNavController.current
		navControllerMap[key] = current
		content(current)
	}
}