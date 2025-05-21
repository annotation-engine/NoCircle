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
		
		private const val FROM_ROUTE_KEY = "FROM_ROUTE"
		private const val RESULT_ROUTE_KEY = "RESULT_ROUTE"
		
		internal val navControllerCacheMap = mutableMapOf<NavController, NoNavHostController>()
		
		val recordRoutes = mutableMapOf<String, KClass<out NoRoute>>()
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
	
	val resultRoute: KClass<out NoRoute>?
		get() = this.getResult<String>(RESULT_ROUTE_KEY)?.let { recordRoutes[it] }
	
	val fromRoute: KClass<out NoRoute>?
		get() = this.getData<String>(FROM_ROUTE_KEY)?.let { recordRoutes[it] }
	
	val currentRoute: KClass<out NoRoute>?
		get() = original.currentDestination?.route?.let { recordRoutes[it] }
	
	fun <R : NoRoute> navigate(
		route: R,
		data: Map<String, Any?>? = null,
		popup: NoPopUp = NoPopUp.None,
		singleTop: Boolean = true,
	) {
		val entry = original.currentBackStackEntry ?: return
		val handle = entry.savedStateHandle
		data?.forEach { (key, value) ->
			checkKey(key)
			handle[key] = value
		}
		handle[FROM_ROUTE_KEY] = original.currentDestination?.route
		
		when (popup) {
			NoPopUp.None -> {
				original.navigate(route) {
					this.launchSingleTop = singleTop
				}
			}
			
			NoPopUp.Current -> {
				val currentRoute = this.currentRoute
				original.navigate(route) {
					this.launchSingleTop = singleTop
					currentRoute?.let {
						popUpTo(it) {
							this.inclusive = true
						}
					}
				}
			}
			
			NoPopUp.All -> {
				original.navigate(route) {
					this.launchSingleTop = singleTop
					popUpTo(0) {
						this.inclusive = true
					}
				}
				val key = NoNavControllerManager.findKey(this)
				if (key == RootNavHost) {
					NoNavControllerManager.removeAllExpectForRoot()
				}
			}
		}
	}
	
	fun popBackStack(vararg data: Pair<String, Any>) {
		val entry = original.previousBackStackEntry ?: return
		val handle = entry.savedStateHandle
		data.forEach { (key, value) ->
			checkKey(key)
			handle[key] = value
		}
		handle[RESULT_ROUTE_KEY] = original.currentDestination?.route
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
	
	private fun checkKey(key: String) {
		check(key != RESULT_ROUTE_KEY && key != FROM_ROUTE_KEY) {
			"不允许使用 $RESULT_ROUTE_KEY 和 $FROM_ROUTE_KEY"
		}
	}
}

enum class NoPopUp {
	None,
	Current,
	All
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