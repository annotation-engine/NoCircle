package com.nocircle.common.expends

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType

fun <T> NavController.getData(key: String, remove: Boolean = true): T? {
	val handle = this.previousBackStackEntry?.savedStateHandle ?: return null
	return handle.get<T>(key).also {
		if (remove) {
			handle.remove<T>(key)
		}
	}
}

fun <T> NavController.getResult(key: String, remove: Boolean = true): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle ?: return null
	return handle.get<T>(key).also {
		if (remove) {
			handle.remove<T>(key)
		}
	}
}

private const val BACK_ROUTE = "NO_CIRCLE_BACK_ROUTE"
private const val LAST_ROUTE = "NO_CIRCLE_LAST_ROUTE"

val NavController.backRoute: KClass<*>?
	get() = this.getResult<String>(BACK_ROUTE)?.let { noRouteMap[it] }

val NavController.lastRoute: KClass<*>?
	get() = this.getResult<String>(LAST_ROUTE)?.let { noRouteMap[it] }

fun <R : Any> NavController.noNavigate(
	route: R,
	data: Map<String, Any?>? = null,
	finish: Boolean = false,
) {
	this.currentBackStackEntry?.savedStateHandle?.let {
		data?.forEach { (key, value) ->
			it[key] = value
		}
		it[LAST_ROUTE] = this.currentDestination?.route
	}
	this.navigate(route) {
		launchSingleTop = true
		if (finish) {
			popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
				inclusive = true
			}
		}
	}
}

fun NavController.noPopBackStack(data: Map<String, Any?>) {
	if (data.isNotEmpty()) {
		this.previousBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
			it[BACK_ROUTE] = this.currentDestination?.route
		}
	}
	this.popBackStack()
}

fun NavController.noPopBackStack(vararg data: Pair<String, Any?>) {
	if (data.isNotEmpty()) {
		this.previousBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
			it[BACK_ROUTE] = this.currentDestination?.route
		}
	}
	this.popBackStack()
}

private val noRouteMap = mutableMapOf<String, KClass<out Any>>()

fun <T : Any> saveNoRouteKClass(kClass: KClass<T>) {
	noRouteMap[kClass.qualifiedName!!] = kClass
}

inline fun <reified T : Any> NavGraphBuilder.noComposable(
	typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
	deepLinks: List<NavDeepLink> = emptyList(),
	noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = null,
	noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = null,
	noinline popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = enterTransition,
	noinline popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = exitTransition,
	noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
	noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
	saveNoRouteKClass(T::class)
	this.composable<T>(
		typeMap,
		deepLinks,
		enterTransition,
		exitTransition,
		popEnterTransition,
		popExitTransition,
		sizeTransform,
		content
	)
}