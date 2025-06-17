package com.nocircle.compose.expends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

@Composable
fun IntSize.toDpSize(): DpSize {
	val density = LocalDensity.current
	return with(density) { DpSize(width.toDp(), height.toDp()) }
}

@Stable
fun IntSize.toDpSize(density: Density): DpSize {
	return with(density) { DpSize(width.toDp(), height.toDp()) }
}