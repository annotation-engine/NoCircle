package com.nocircle.app.pages.main

import androidx.compose.runtime.Composable

@Composable
actual fun LeftBarItemTooltip(
	tooltipText: String,
	isExpended: Boolean,
	content: @Composable (() -> Unit)
) = content()