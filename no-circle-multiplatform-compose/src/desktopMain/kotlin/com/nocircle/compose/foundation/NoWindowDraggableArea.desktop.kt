package com.nocircle.compose.foundation

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.FrameWindowScope

@Composable
actual fun NoWindowDraggableArea(content: @Composable (() -> Unit)) {
	LocalFrameWindowScope.current.WindowDraggableArea {
		content()
	}
}

val LocalFrameWindowScope = staticCompositionLocalOf<FrameWindowScope> {
	error("CompositionLocal LocalFrameWindowScope not present")
}