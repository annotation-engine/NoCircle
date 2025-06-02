package com.nocircle.compose.pointer

import androidx.compose.ui.input.pointer.PointerIcon
import org.jetbrains.skiko.Cursor

val PointerIcon.Companion.WestResizeCursor by lazy {
    PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))
}

val PointerIcon.Companion.NorthResizeCursor by lazy {
    PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))
}