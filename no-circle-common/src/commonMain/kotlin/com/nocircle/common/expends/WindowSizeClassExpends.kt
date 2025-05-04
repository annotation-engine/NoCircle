@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.expends

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
@ExperimentalMaterial3WindowSizeClassApi
expect inline fun calculateWindowSizeClass(): WindowSizeClass

@Composable
@ExperimentalMaterial3WindowSizeClassApi
inline fun calculateWindowWidthSizeClass(): WindowWidthSizeClass {
	return calculateWindowSizeClass().widthSizeClass
}

@Composable
@ExperimentalMaterial3WindowSizeClassApi
inline fun calculateWindowHeightSizeClass(): WindowHeightSizeClass {
	return calculateWindowSizeClass().heightSizeClass
}