package com.nocircle.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedState
import kotlin.reflect.KClass

class NoNavHostController internal constructor(
	val original: NavHostController
) {
	
	companion object {
		
		private val BACK_ROUTE_KEY = "BACK_ROUTE_${(0 .. Int.MAX_VALUE).random()}"
		private val LAST_ROUTE_KEY = "LAST_ROUTE_${(0 .. Int.MAX_VALUE).random()}"
		
		internal val navControllerCacheMap = mutableMapOf<NavController, NoNavHostController>()
		
		val AllRouteKClasses = mutableMapOf<String, KClass<out NoRoute>>()
	}
	
	fun <T> getData(key: String, remove: Boolean = true): T? {
		val handle = original.previousBackStackEntry
			?.savedStateHandle ?: return null
		return handle.get<T>(key).also {
			if (remove) handle.remove<T>(key)
		}
	}
	
	fun <T> getResult(key: String, remove: Boolean = true): T? {
		val handle = original.currentBackStackEntry
			?.savedStateHandle ?: return null
		return handle.get<T>(key).also {
			if (remove) handle.remove<T>(key)
		}
	}
	
	val backRoute: KClass<out NoRoute>?
		get() = this.getResult<String>(BACK_ROUTE_KEY)?.let {
			AllRouteKClasses[it]
		}
	
	val lastRoute: KClass<out NoRoute>?
		get() = this.getData<String>(LAST_ROUTE_KEY)?.let {
			AllRouteKClasses[it]
		}
	
	val currentRoute: KClass<out NoRoute>?
		get() = original.currentDestination?.route?.let {
			AllRouteKClasses[it]
		}
	
	fun <R : NoRoute> navigate(
		route: R,
		data: Map<String, Any?>? = null,
		finish: Boolean = false
	) {
		original.currentBackStackEntry?.savedStateHandle?.let {
			data?.forEach { (key, value) ->
				it[key] = value
			}
			it[LAST_ROUTE_KEY] = original.currentDestination?.route
		}
		original.navigate(route) {
			launchSingleTop = true
			if (finish) {
				val currentRoute = original.currentBackStackEntry
					?.destination?.route ?: return@navigate
				popUpTo(currentRoute) {
					inclusive = true
				}
			}
		}
	}
	
	fun popBackStack(vararg data: Pair<String, Any>) {
		original.previousBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
			it[LAST_ROUTE_KEY] = original.currentDestination?.route
		}
		original.popBackStack()
	}
	
	fun addOnDestinationChangedListener(listener: NoOnDestinationChangedListener) {
		original.addOnDestinationChangedListener { controller, destination, arguments ->
			listener.onDestinationChanged(
				navControllerCacheMap.getOrPut(controller) {
					NoNavHostController(controller as NavHostController)
				},
				destination,
				arguments
			)
		}
	}
	
	fun interface NoOnDestinationChangedListener {
		
		fun onDestinationChanged(controller: NoNavHostController, destination: NavDestination, arguments: SavedState?)
	}
}

@Composable
fun rememberNoNavController(): NoNavHostController {
	val navController = rememberNavController()
	return remember(navController) {
		NoNavHostController.navControllerCacheMap.getOrPut(navController) {
			NoNavHostController(navController)
		}
	}
}