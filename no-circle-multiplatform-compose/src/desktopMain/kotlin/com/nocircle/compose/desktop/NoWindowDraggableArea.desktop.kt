package com.nocircle.compose.desktop

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.FrameWindowScope
import com.nocircle.compose.expends.noLocalProvidedFor

@Composable
actual fun NoWindowDraggableArea(content: @Composable (() -> Unit)) {
	LocalFrameWindowScope.current.WindowDraggableArea {
		content()
	}
}

val LocalFrameWindowScope = staticCompositionLocalOf<FrameWindowScope> {
	noLocalProvidedFor("LocalFrameWindowScope")
}