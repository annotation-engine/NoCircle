package com.nocircle.compose.complex

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.nocircle.compose.pointer.WestResizeCursor

@Stable
internal actual fun Modifier.pointerResizeHorizontalHoverIcon(): Modifier {
	return this.pointerHoverIcon(PointerIcon.WestResizeCursor)
}