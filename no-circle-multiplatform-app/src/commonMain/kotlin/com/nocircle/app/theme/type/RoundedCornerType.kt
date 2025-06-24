package com.nocircle.app.theme.type

import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.settings.memory.freeMemory
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StatusFlowConfig
import kotlinx.serialization.Serializable

@Serializable
enum class RoundedCornerType(
	val extraSmall: Dp,
	val small: Dp,
	val medium: Dp,
	val large: Dp,
	val extraLarge: Dp
) {
	EXTRA_SMALL(
		extraSmall = 1.dp,
		small = 2.dp,
		medium = 3.dp,
		large = 4.dp,
		extraLarge = 7.dp
	),
	SMALL(
		extraSmall = 2.dp,
		small = 4.dp,
		medium = 6.dp,
		large = 8.dp,
		extraLarge = 14.dp
	),
	NORMAL(
		extraSmall = 4.dp,
		small = 8.dp,
		medium = 12.dp,
		large = 16.dp,
		extraLarge = 28.dp
	),
	LARGE(
		extraSmall = 6.dp,
		small = 12.dp,
		medium = 18.dp,
		large = 24.dp,
		extraLarge = 42.dp
	),
	EXTRA_LARGE(
		extraSmall = 8.dp,
		small = 16.dp,
		medium = 24.dp,
		large = 32.dp,
		extraLarge = 56.dp
	);
	
	companion object Companion : StatusFlowConfig<RoundedCornerType>() {
		
		override suspend fun getConfigFromStorage(): RoundedCornerType {
			return RoundedCornerTypeConfigKey.get() ?: NORMAL
		}
		
		override suspend fun setConfigToStorage(oldConfig: RoundedCornerType, newConfig: RoundedCornerType) {
			RoundedCornerTypeConfigKey.set(newConfig)
		}
	}
}

private object RoundedCornerTypeConfigKey : ConfigKey<RoundedCornerType>("roundedCornerType")

private var currentType: RoundedCornerType? = null
private var currentShapes: Shapes? = null

@Composable
fun animateShapes(): Shapes {
	val type = RoundedCornerType.current
	if (currentType == type && currentShapes != null) {
		return currentShapes!!
	}
	val updateTransition = updateTransition(type, label = "CornersTransition")
	return Shapes(
		extraSmall = updateTransition.animateRoundedCornerShape { it.extraSmall }.value,
		small = updateTransition.animateRoundedCornerShape { it.small }.value,
		medium = updateTransition.animateRoundedCornerShape { it.medium }.value,
		large = updateTransition.animateRoundedCornerShape { it.large }.value,
		extraLarge = updateTransition.animateRoundedCornerShape { it.extraLarge }.value,
	).also {
		LaunchedEffect(updateTransition.isRunning) {
			if (!updateTransition.isRunning) {
				currentType = type
				currentShapes = it
				freeMemory()
			}
		}
	}
}

@Composable
private inline fun <S> Transition<S>.animateRoundedCornerShape(
	noinline transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Dp> = {
		spring(visibilityThreshold = Dp.VisibilityThreshold)
	},
	label: String = "RoundedCornerShapeAnimation",
	targetValueByState: @Composable() (state: S) -> Dp
): State<RoundedCornerShape> {
	val value by this.animateDp(transitionSpec, label, targetValueByState)
	return remember(value) {
		derivedStateOf { RoundedCornerShape(value) }
	}
}