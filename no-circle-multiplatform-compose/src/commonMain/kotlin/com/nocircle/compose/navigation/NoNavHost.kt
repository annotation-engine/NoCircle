package com.nocircle.compose.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nocircle.compose.expends.noLocalProvidedFor
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
	navPopTransition: NavPopTransition = NavPopTransition.HorizontalSlide,
	builder: NoNavGraphBuilder.() -> Unit
) {
	NavHost(
		navController = navController.original,
		graph = remember(startDestination, route) {
			navController.original.createGraph(startDestination, route) {
				NoNavGraphBuilder(this).builder()
			}
		},
		modifier = modifier,
		contentAlignment = contentAlignment,
		enterTransition = { navTransition.enter },
		exitTransition = { navTransition.exit },
		popEnterTransition = { navPopTransition.popEnter },
		popExitTransition = { navPopTransition.popExit }
	)
}

val LocalNavController = staticCompositionLocalOf<NoNavHostController> {
	noLocalProvidedFor("LocalNavController")
}

@Immutable
sealed interface NavTransition {
	
	val enter: EnterTransition
	
	val exit: ExitTransition
	
	@Immutable
	object HorizontalSlide : NavTransition {
		
		override val enter = slideInHorizontally(
			initialOffsetX = { it },
			animationSpec = SlideTween
		)
		
		override val exit = slideOutHorizontally(
			targetOffsetX = { -it / 5 },
			animationSpec = SlideTween
		)
	}
	
	@Immutable
	object Fade : NavTransition {
		
		override val enter = fadeIn(animationSpec = FadeTween)
		
		override val exit = fadeOut(animationSpec = FadeTween)
	}
	
	@Immutable
	object None : NavTransition {
		
		override val enter = EnterTransition.None
		
		override val exit = ExitTransition.None
	}
}

@Immutable
sealed interface NavPopTransition {
	
	val popEnter: EnterTransition
	
	val popExit: ExitTransition
	
	@Immutable
	object HorizontalSlide : NavPopTransition {
		
		override val popEnter = slideInHorizontally(
			initialOffsetX = { -it / 5 },
			animationSpec = SlideTween
		)
		
		override val popExit = slideOutHorizontally(
			targetOffsetX = { it },
			animationSpec = SlideTween
		)
	}
	
	@Immutable
	object Fade : NavPopTransition {
		
		override val popEnter = fadeIn(animationSpec = FadeTween)
		
		override val popExit = fadeOut(animationSpec = FadeTween)
	}
	
	@Immutable
	object None : NavPopTransition {
		
		override val popEnter = EnterTransition.None
		
		override val popExit = ExitTransition.None
	}
}

private val SlideTween = tween<IntOffset>(300)
private val FadeTween = tween<Float>(120)

@JvmInline
value class NoNavGraphBuilder(
	val original: NavGraphBuilder,
)

inline fun <reified T : NoRoute> NoNavGraphBuilder.composable(
	typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
	deepLinks: List<NavDeepLink> = emptyList(),
	navTransition: NavTransition? = null,
	navPopTransition: NavPopTransition? = null,
	noinline sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
	noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
	NoNavHostController.recordRouteMapping(T::class.qualifiedName!!, route = T::class)
	original.composable<T>(
		typeMap = typeMap,
		deepLinks = deepLinks,
		enterTransition = { navTransition?.enter },
		exitTransition = { navTransition?.exit },
		popEnterTransition = { navPopTransition?.popEnter },
		popExitTransition = { navPopTransition?.popExit },
		sizeTransform = sizeTransform,
		content = content
	)
}