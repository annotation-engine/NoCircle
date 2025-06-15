package com.nocircle.compose.material3

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapTo
import com.nocircle.common.expends.noLocalProvidedFor
import kotlinx.coroutines.delay

@Composable
fun NoSnackbarHost(
	hostState: SnackbarHostState,
	modifier: Modifier = Modifier,
	snackbar: @Composable (SnackbarData) -> Unit = { NoSnackbar(it) }
) {
	val currentSnackbarData = hostState.currentSnackbarData
	val accessibilityManager = LocalAccessibilityManager.current
	LaunchedEffect(currentSnackbarData) {
		if (currentSnackbarData != null) {
			val visuals = currentSnackbarData.visuals as NoSnackbarVisuals
			val duration = visuals.duration.toMillis(
				currentSnackbarData.visuals.actionLabel != null,
				accessibilityManager
			)
			delay(duration)
			currentSnackbarData.dismiss()
		}
	}
	
	NoFadeInFadeOutWithScale(
		current = hostState.currentSnackbarData,
		modifier = modifier,
		content = snackbar
	)
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
	noLocalProvidedFor("LocalSnackbarHostState")
}

@Stable
private fun SnackbarDuration.toMillis(
	hasAction: Boolean,
	accessibilityManager: AccessibilityManager?
): Long {
	val original = when (this) {
		SnackbarDuration.Indefinite -> Long.MAX_VALUE
		SnackbarDuration.Long -> 8_000L
		SnackbarDuration.Short -> 2_000L
	}
	if (accessibilityManager == null) {
		return original
	}
	return accessibilityManager.calculateRecommendedTimeoutMillis(
		original,
		containsIcons = true,
		containsText = true,
		containsControls = hasAction
	)
}

@Composable
private fun NoFadeInFadeOutWithScale(
	current: SnackbarData?,
	modifier: Modifier = Modifier,
	content: @Composable (SnackbarData) -> Unit
) {
	val state = remember { FadeInFadeOutState<SnackbarData?>() }
	if (current != state.current) {
		state.current = current
		val keys = state.items.fastMap { it.key }.toMutableList()
		if (current !in keys) {
			keys += current
		}
		state.items.clear()
		keys.fastFilterNotNull().fastMapTo(state.items) { key ->
			FadeInFadeOutAnimationItem(key) { children ->
				val isVisible = key == current
				val duration = if (isVisible) SnackbarFadeInMillis else SnackbarFadeOutMillis
				val delay = SnackbarFadeOutMillis + SnackbarInBetweenDelayMillis
				val animationDelay =
					if (isVisible && keys.fastFilterNotNull().size != 1) {
						delay
					} else 0
				val opacity = animatedOpacity(
					animation = tween(
						easing = LinearEasing,
						delayMillis = animationDelay,
						durationMillis = duration
					),
					visible = isVisible,
					onAnimationFinish = {
						if (key != state.current) {
							state.items.removeAll { it.key == key }
							state.scope?.invalidate()
						}
					}
				)
				val scale = animatedScale(
					animation = tween(
						easing = FastOutSlowInEasing,
						delayMillis = animationDelay,
						durationMillis = duration
					),
					visible = isVisible
				)
				Box(
					Modifier
						.graphicsLayer(
							scaleX = scale.value,
							scaleY = scale.value,
							alpha = opacity.value
						)
						.semantics {
							liveRegion = LiveRegionMode.Polite
							dismiss {
								key.dismiss()
								true
							}
						}
				) {
					children()
				}
			}
		}
	}
	Box(modifier) {
		state.scope = currentRecomposeScope
		state.items.fastForEach { (item, opacity) ->
			key(item) { opacity { content(item!!) } }
		}
	}
}

private class FadeInFadeOutState<T> {
	var current: Any? = Any()
	var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
	var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
	val key: T,
	val transition: FadeInFadeOutTransition
)

private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
private fun animatedOpacity(
	animation: AnimationSpec<Float>,
	visible: Boolean,
	onAnimationFinish: () -> Unit = {}
): State<Float> {
	val alpha = remember { Animatable(if (!visible) 1f else 0f) }
	LaunchedEffect(visible) {
		alpha.animateTo(if (visible) 1f else 0f, animationSpec = animation)
		onAnimationFinish()
	}
	return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
	val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
	LaunchedEffect(visible) {
		scale.animateTo(if (visible) 1f else 0.8f, animationSpec = animation)
	}
	return scale.asState()
}

private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0