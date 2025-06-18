package com.nocircle.compose.desktop

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import com.nocircle.compose.expends.noLocalProvidedFor

@Composable
actual fun NoWindowDraggableArea(
	modifier: Modifier,
	content: @Composable (() -> Unit)
) {
	LocalFrameWindowScope.current.WindowDraggableArea(
		modifier = modifier
	) {
		content()
	}
}

val LocalFrameWindowScope = staticCompositionLocalOf<FrameWindowScope> {
	noLocalProvidedFor("LocalFrameWindowScope")
}