package com.nocircle.compose.foundation.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nocircle.common.windowsize.WindowWidthSizes

@Composable
fun Modifier.autoPadding(paddingValues: PaddingValues): Modifier {
	if (!WindowWidthSizes.isCompact) return this
	return this.padding(top = paddingValues.calculateTopPadding())
}