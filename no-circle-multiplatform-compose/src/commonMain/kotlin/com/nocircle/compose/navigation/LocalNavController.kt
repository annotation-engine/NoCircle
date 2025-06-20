package com.nocircle.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
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

val NavController.currentSavedStateHandle: SavedStateHandle?
	get() = this.currentBackStackEntry?.savedStateHandle

val NavController.previousSavedStateHandle: SavedStateHandle?
	get() = this.previousBackStackEntry?.savedStateHandle

inline fun <reified R : NoRoute> NavController.isRoute(): Boolean {
	val route = this.currentDestination?.route ?: return false
	return route == R::class.qualifiedName
}

inline fun <reified R : NoRoute> NavController.isNotRoute(): Boolean {
	val route = this.currentDestination?.route ?: return true
	return route != R::class.qualifiedName
}