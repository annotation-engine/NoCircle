package com.nocircle.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedState
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

@JvmInline
value class NoNavHostController internal constructor(
	val original: NavHostController
) {
	
	companion object {
		
		private val routeMappingTables = mutableMapOf<String, KClass<out NoRoute>>()
		
		private const val RESULT_ROUTE_KEY = "RESULT_ROUTE"
		private const val FROM_ROUTE_KEY = "FROM_ROUTE"
		
		fun recordRouteMapping(key: String, route: KClass<out NoRoute>) {
			routeMappingTables[key] = route
		}
		
		private val onDestinationChangedListenerMap = mutableMapOf<NoNavHostController, MutableMap<OnDestinationChangedListener, NavController.OnDestinationChangedListener>>()
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
		get() = this.getResult<String>(RESULT_ROUTE_KEY)?.let { routeMappingTables[it]!! }
	
	val fromRoute: KClass<out NoRoute>?
		get() = this.getData<String>(FROM_ROUTE_KEY)?.let { routeMappingTables[it]!! }
	
	val currentRoute: KClass<out NoRoute>?
		get() = original.currentDestination?.route?.let { routeMappingTables[it]!! }
	
	fun <R : NoRoute> navigate(
		route: R,
		data: Map<String, Any?>? = null,
		popup: NoPopUp = NoPopUp.None,
		singleTop: Boolean = true,
	) {
		val entry = original.currentBackStackEntry ?: return
		val handle = entry.savedStateHandle
		data?.forEach { (key, value) ->
			check(key != FROM_ROUTE_KEY && key != RESULT_ROUTE_KEY)
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
			}
		}
	}
	
	fun popBackStack(vararg data: Pair<String, Any>) {
		val entry = original.previousBackStackEntry ?: return
		val handle = entry.savedStateHandle
		data.forEach { (key, value) ->
			check(key != FROM_ROUTE_KEY && key != RESULT_ROUTE_KEY)
			handle[key] = value
		}
		handle[RESULT_ROUTE_KEY] = original.currentDestination?.route
		original.popBackStack()
	}
	
	fun addOnDestinationChangedListener(listener: OnDestinationChangedListener) {
		this.original.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
			override fun onDestinationChanged(
				controller: NavController,
				destination: NavDestination,
				arguments: SavedState?
			) {
				listener.onDestinationChanged(
					NoNavHostController(controller as NavHostController),
					destination,
					arguments
				)
			}
		}.also { onDestinationChangedListenerMap.getOrPut(this) { mutableMapOf() }[listener] = it })
	}
	
	fun removeOnDestinationChangedListener(listener: OnDestinationChangedListener) {
		val listener = onDestinationChangedListenerMap[this]?.remove(listener) ?: return
		this.original.removeOnDestinationChangedListener(listener)
	}
	
	fun interface OnDestinationChangedListener {
		
		fun onDestinationChanged(controller: NoNavHostController, destination: NavDestination, arguments: SavedState?)
	}
}

enum class NoPopUp {
	None,
	Current,
	All
}

@Composable
fun rememberNoNavController(): NoNavHostController {
	val controller = rememberNavController()
	return remember(controller) { NoNavHostController(controller) }
}

@Composable
fun LocalNavControllerProvider(
	content: @Composable (NoNavHostController) -> Unit,
) {
	CompositionLocalProvider(
		LocalNavController provides rememberNoNavController()
	) {
		content(LocalNavController.current)
	}
}