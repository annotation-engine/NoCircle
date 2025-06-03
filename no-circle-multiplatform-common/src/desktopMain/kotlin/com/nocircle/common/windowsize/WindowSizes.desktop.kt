@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.windowsize

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
actual inline fun calculateWindowSize(): WindowSizeClass {
	return calculateWindowSizeClass()
}