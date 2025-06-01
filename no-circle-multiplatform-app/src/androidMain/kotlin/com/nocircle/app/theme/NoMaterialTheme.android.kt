package com.nocircle.app.theme

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.nocircle.common.utils.Globals

actual fun onDarkModeChanged(isDarkTheme: Boolean) {
    val window = Globals.getActivity().window
    WindowCompat.setDecorFitsSystemWindows(window, true)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.isAppearanceLightStatusBars = !isDarkTheme
    controller.isAppearanceLightNavigationBars = !isDarkTheme
}