@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.expends

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@Composable
@ExperimentalMaterial3WindowSizeClassApi
actual inline fun calculateWindowSize(): WindowSize = calculateWindowSizeClass()