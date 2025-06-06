package com.nocircle.compose.animation

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun animateDpSizeAsState(
	targetValue: DpSize,
	animationSpec: AnimationSpec<DpSize> = spring(),
	label: String = "NoDpSizeAnimation",
	finishedListener: ((DpSize) -> Unit)? = null,
): State<DpSize> = animateValueAsState(
	targetValue = targetValue,
	typeConverter = DpSizeConverter,
	animationSpec = animationSpec,
	label = label,
	finishedListener = finishedListener
)

private object DpSizeConverter : TwoWayConverter<DpSize, AnimationVector2D> {
	
	override val convertToVector: (DpSize) -> AnimationVector2D
		get() = { size -> AnimationVector2D(size.width.value, size.height.value) }
	
	override val convertFromVector: (AnimationVector2D) -> DpSize
		get() = { vector -> DpSize(vector.v1.dp, vector.v2.dp) }
}