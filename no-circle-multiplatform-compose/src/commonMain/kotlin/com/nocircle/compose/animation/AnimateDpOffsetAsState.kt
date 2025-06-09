package com.nocircle.compose.animation

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun animateDpOffsetAsState(
	targetValue: DpOffset,
	animationSpec: AnimationSpec<DpOffset> = spring(),
	label: String = "NoDpOffsetAnimation",
	finishedListener: ((DpOffset) -> Unit)? = null,
): State<DpOffset> = animateValueAsState(
	targetValue = targetValue,
	typeConverter = DpOffsetConverter,
	animationSpec = animationSpec,
	label = label,
	finishedListener = finishedListener
)

private object DpOffsetConverter : TwoWayConverter<DpOffset, AnimationVector2D> {
	
	override val convertToVector: (DpOffset) -> AnimationVector2D
		get() = { size -> AnimationVector2D(size.x.value, size.y.value) }
	
	override val convertFromVector: (AnimationVector2D) -> DpOffset
		get() = { vector -> DpOffset(vector.v1.dp, vector.v2.dp) }
}