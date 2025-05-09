package com.nocircle.common.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Composable
fun NoNavHost(
	navController: NoNavHostController,
	startDestination: NoRoute,
	modifier: Modifier = Modifier,
	contentAlignment: Alignment = Alignment.TopStart,
	route: KClass<*>? = null,
	enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = { DefaultEnterTransition },
	exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = { DefaultExitTransition },
	popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = { DefaultPopEnterTransition },
	popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = { DefaultPopExitTransition },
	builder: NoNavGraphBuilder.() -> Unit
) {
	NavHost(
		navController = navController.original,
		graph = remember(route, startDestination, builder) {
			navController.original.createGraph(startDestination, route) {
				NoNavGraphBuilder(this).builder()
			}
		},
		modifier = modifier,
		contentAlignment = contentAlignment,
		enterTransition = enterTransition,
		exitTransition = exitTransition,
		popEnterTransition = popEnterTransition,
		popExitTransition = popExitTransition
	)
}

private val DefaultEnterTransition = slideInHorizontally(
	initialOffsetX = { it },
	animationSpec = tween(300)
)

private val DefaultExitTransition = slideOutHorizontally(
	targetOffsetX = { -it },
	animationSpec = tween(300)
)

private val DefaultPopEnterTransition = slideInHorizontally(
	initialOffsetX = { -it },
	animationSpec = tween(300)
)

private val DefaultPopExitTransition = slideOutHorizontally(
	targetOffsetX = { it },
	animationSpec = tween(300)
)

class NoNavGraphBuilder(
	val original: NavGraphBuilder,
) {
	
	inline fun <reified T : NoRoute> composable(
		typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
		deepLinks: List<NavDeepLink> = emptyList(),
		noinline enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = null,
		noinline exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = null,
		noinline popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = enterTransition,
		noinline popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = exitTransition,
		noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
		noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
	) {
		NoNavHostController.AllRouteKClasses[T::class.qualifiedName!!] = T::class
		original.composable<T>(
			typeMap = typeMap,
			deepLinks = deepLinks,
			enterTransition = enterTransition,
			exitTransition = exitTransition,
			popEnterTransition = popEnterTransition,
			popExitTransition = popExitTransition,
			sizeTransform = sizeTransform,
			content = content
		)
	}
}