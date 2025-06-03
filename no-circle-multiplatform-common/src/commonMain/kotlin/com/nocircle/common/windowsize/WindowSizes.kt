@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.windowsize

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Composable
expect inline fun calculateWindowSize(): WindowSizeClass

@Composable
inline fun calculateWindowWidthSize(): WindowWidthSizeClass {
	return calculateWindowSize().widthSizeClass
}

@Composable
inline fun calculateWindowHeightSize(): WindowHeightSizeClass {
	return calculateWindowSize().heightSizeClass
}

object WindowWidthSizes {
	
	inline val isCompact: Boolean
		@Stable
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSizeClass.Compact
	
	inline val isMedium: Boolean
		@Stable
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSizeClass.Medium
	
	inline val isExpended: Boolean
		@Stable
		@Composable
		get() = calculateWindowWidthSize() == WindowWidthSizeClass.Expanded
}

object WindowHeightSizes {
	
	inline val isCompact: Boolean
		@Stable
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSizeClass.Compact
	
	inline val isMedium: Boolean
		@Stable
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSizeClass.Medium
	
	inline val isExpended: Boolean
		@Stable
		@Composable
		get() = calculateWindowHeightSize() == WindowHeightSizeClass.Expanded
}