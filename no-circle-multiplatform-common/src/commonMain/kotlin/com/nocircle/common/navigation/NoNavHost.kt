package com.nocircle.common.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlin.jvm.JvmInline
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
	navTransition: NavTransition = NavTransition.HorizontalSlide,
	builder: NoNavGraphBuilder.() -> Unit
) {
	CompositionLocalProvider(
		LocalNavController provides navController
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
			enterTransition = { navTransition.enter },
			exitTransition = { navTransition.exit },
			popEnterTransition = { navTransition.popEnter },
			popExitTransition = { navTransition.popExit }
		)
	}
}

val LocalNavController = staticCompositionLocalOf<NoNavHostController> {
	error("CompositionLocal LocalNavController not present")
}

sealed interface NavTransition {
	
	val enter: EnterTransition
	
	val exit: ExitTransition
	
	val popEnter: EnterTransition
	
	val popExit: ExitTransition
	
	object HorizontalSlide : NavTransition {
		
		override val enter = slideInHorizontally(
			initialOffsetX = { it },
			animationSpec = tween(300)
		)
		
		override val exit = slideOutHorizontally(
			targetOffsetX = { -it / 5 },
			animationSpec = tween(300)
		)
		
		override val popEnter = slideInHorizontally(
			initialOffsetX = { -it / 5 },
			animationSpec = tween(300)
		)
		
		override val popExit = slideOutHorizontally(
			targetOffsetX = { it },
			animationSpec = tween(300)
		)
	}
	
	object Fade : NavTransition {
		
		override val enter = fadeIn(animationSpec = tween(120))
		
		override val exit = fadeOut(animationSpec = tween(120))
		
		override val popEnter = enter
		
		override val popExit = exit
	}
	
	object None : NavTransition {
		
		override val enter = EnterTransition.None
		
		override val exit = ExitTransition.None
		
		override val popEnter = enter
		
		override val popExit = exit
	}
}

@JvmInline
value class NoNavGraphBuilder(
	val original: NavGraphBuilder,
)

inline fun <reified T : NoRoute> NoNavGraphBuilder.composable(
	typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
	deepLinks: List<NavDeepLink> = emptyList(),
	navTransition: NavTransition? = null,
	noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
	noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
	recordRoute(T::class.qualifiedName!!, route = T::class)
	original.composable<T>(
		typeMap = typeMap,
		deepLinks = deepLinks,
		enterTransition = { navTransition?.enter },
		exitTransition = { navTransition?.exit },
		popEnterTransition = { navTransition?.popEnter },
		popExitTransition = { navTransition?.popExit },
		sizeTransform = sizeTransform,
		content = content
	)
}