package com.nocircle.compose.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import kotlin.jvm.JvmSuppressWildcards

@Immutable
sealed interface TransitionScope {
	
	val dampingRatio: Float
	
	val stiffness: Float
}

@Immutable
sealed interface EnterTransitionScope : TransitionScope

@Immutable
sealed interface ExitTransitionScope : TransitionScope

@Immutable
sealed interface PopEnterTransitionScope : TransitionScope

@Immutable
sealed interface PopExitTransitionScope : TransitionScope

@Immutable
private class EnterTransitionScopeImpl(
	override val dampingRatio: Float,
	override val stiffness: Float
) : EnterTransitionScope

@Immutable
private class ExitTransitionScopeImpl(
	override val dampingRatio: Float,
	override val stiffness: Float
) : ExitTransitionScope

@Immutable
private class PopEnterTransitionScopeImpl(
	override val dampingRatio: Float,
	override val stiffness: Float
) : PopEnterTransitionScope

@Immutable
private class PopExitTransitionScopeImpl(
	override val dampingRatio: Float,
	override val stiffness: Float
) : PopExitTransitionScope

@Stable
fun EnterTransitionScope.horizontalSlider(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
) = slideInHorizontally(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness,
		visibilityThreshold = IntOffset.VisibilityThreshold
	),
	initialOffsetX = { it }
)

@Stable
fun ExitTransitionScope.horizontalSlider(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
) = slideOutHorizontally(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness,
		visibilityThreshold = IntOffset.VisibilityThreshold
	),
	targetOffsetX = { -it / 5 }
)

@Stable
fun PopEnterTransitionScope.horizontalSlider(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
) = slideInHorizontally(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness,
		visibilityThreshold = IntOffset.VisibilityThreshold
	),
	initialOffsetX = { -it / 5 }
)

@Stable
fun PopExitTransitionScope.horizontalSlider(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
) = slideOutHorizontally(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness,
		visibilityThreshold = IntOffset.VisibilityThreshold
	),
	targetOffsetX = { it }
)

@Stable
fun EnterTransitionScope.fade(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
	initialAlpha: Float = 0f,
) = fadeIn(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness
	),
	initialAlpha = initialAlpha
)

@Stable
fun ExitTransitionScope.fade(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
	targetAlpha: Float = 0f,
) = fadeOut(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness
	),
	targetAlpha = targetAlpha
)

@Stable
fun PopEnterTransitionScope.fade(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
	initialAlpha: Float = 0f,
) = fadeIn(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness
	),
	initialAlpha = initialAlpha
)

@Stable
fun PopExitTransitionScope.fade(
	dampingRatio: Float = this.dampingRatio,
	stiffness: Float = this.stiffness,
	targetAlpha: Float = 0f,
) = fadeOut(
	animationSpec = spring(
		dampingRatio = dampingRatio,
		stiffness = stiffness
	),
	targetAlpha = targetAlpha
)

val EnterTransitionScope.none
	@Stable
	get() = EnterTransition.None

val ExitTransitionScope.none
	@Stable
	get() = ExitTransition.None

val PopEnterTransitionScope.none
	@Stable
	get() = EnterTransition.None

val PopExitTransitionScope.none
	@Stable
	get() = ExitTransition.None

private typealias EnterTransitionLambda = @JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
private typealias ExitTransitionLambda = @JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition

private const val NavTransitionStiffness = 1000f

@Stable
fun enterTransition(
	dampingRatio: Float = Spring.DampingRatioNoBouncy,
	stiffness: Float = NavTransitionStiffness,
	scope: EnterTransitionScope.() -> EnterTransition
): EnterTransitionLambda = { EnterTransitionScopeImpl(dampingRatio, stiffness).scope() }

@Stable
fun exitTransition(
	dampingRatio: Float = Spring.DampingRatioNoBouncy,
	stiffness: Float = NavTransitionStiffness,
	scope: ExitTransitionScope.() -> ExitTransition
): ExitTransitionLambda = { ExitTransitionScopeImpl(dampingRatio, stiffness).scope() }

@Stable
fun popEnterTransition(
	dampingRatio: Float = Spring.DampingRatioNoBouncy,
	stiffness: Float = NavTransitionStiffness,
	scope: PopEnterTransitionScope.() -> EnterTransition
): EnterTransitionLambda = { PopEnterTransitionScopeImpl(dampingRatio, stiffness).scope() }

@Stable
fun popExitTransition(
	dampingRatio: Float = Spring.DampingRatioNoBouncy,
	stiffness: Float = NavTransitionStiffness,
	scope: PopExitTransitionScope.() -> ExitTransition
): ExitTransitionLambda = { PopExitTransitionScopeImpl(dampingRatio, stiffness).scope() }