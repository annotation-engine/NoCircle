package com.nocircle.compose.compose

import androidx.compose.runtime.Composable

@Composable
fun <T : Any> NoSkeleton(
	data: T?,
	content: @Composable (T) -> Unit
) {
	if (data == null) {
	
	} else {
		content(data)
	}
}