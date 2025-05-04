@file:Suppress("NOTHING_TO_INLINE")

package com.nocircle.common.expends

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import com.nocircle.common.utils.Globals

@Composable
@ExperimentalMaterial3WindowSizeClassApi
actual inline fun calculateWindowSizeClass(): WindowSizeClass = calculateWindowSizeClass(Globals.getActivity())