package com.nocircle.compose.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import kotlin.jvm.JvmSuppressWildcards

private typealias EnterTransitionLambda = @JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
private typealias ExitTransitionLambda = @JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition

object HorizontalSlideTransition {
	
	private val slideTween = tween<IntOffset>(300)
	
	private val enter = slideInHorizontally(slideTween) { it }
	private val exit = slideOutHorizontally(slideTween) { -it / 5 }
	private val popEnter = slideInHorizontally(slideTween) { -it / 5 }
	private val popExit = slideOutHorizontally(slideTween) { it }
	
	val Enter: EnterTransitionLambda = { enter }
	val Exit: ExitTransitionLambda = { exit }
	val PopEnter: EnterTransitionLambda = { popEnter }
	val PopExit: ExitTransitionLambda = { popExit }
}

object FadeTransition {
	
	private val fadeTween = tween<Float>(150)
	
	private val enter = fadeIn(fadeTween)
	private val exit = fadeOut(fadeTween)
	
	val Enter: EnterTransitionLambda = { enter }
	val Exit: ExitTransitionLambda = { exit }
	val PopEnter = Enter
	val PopExit = Exit
}

object NoneTransition {
	
	val Enter: EnterTransitionLambda = { EnterTransition.None }
	val Exit: ExitTransitionLambda = { ExitTransition.None }
}