@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.windowsize

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

typealias WindowSize = WindowSizeClass
typealias WindowWidthSize = WindowWidthSizeClass
typealias WindowHeightSize = WindowHeightSizeClass

@Composable
expect inline fun calculateWindowSize(): WindowSize

@Composable
inline fun calculateWindowWidthSize(): WindowWidthSize {
	return calculateWindowSize().widthSizeClass
}

@Composable
inline fun calculateWindowHeightSize(): WindowHeightSize {
	return calculateWindowSize().heightSizeClass
}

object WindowWidthSizes {
	
	inline val isCompact: Boolean
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSize.Compact
	
	inline val isMedium: Boolean
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSize.Medium
	
	inline val isExpended: Boolean
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSize.Expanded
}

object WindowHeightSizes {
	
	inline val isCompact: Boolean
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSize.Compact
	
	inline val isMedium: Boolean
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSize.Medium
	
	inline val isExpended: Boolean
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSize.Expanded
}