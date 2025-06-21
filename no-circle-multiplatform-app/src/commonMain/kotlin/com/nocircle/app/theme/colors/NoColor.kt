package com.nocircle.app.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nocircle.app.theme.scheme.ColorSchemeConfig

class NoColor private constructor(
	private val light: Color,
	private val dark: Color
) {
	
	val current: Color
		@Composable
		get() = if (ColorSchemeConfig.current.themeMode.isDark) dark else light
	
	companion object {
		
		private val green = NoColor(
			Color(0xFF4CAF50),
			Color(0xFF81C784)
		)
		
		val Green: Color
			@Composable
			get() = green.current
		
		private val yellow = NoColor(
			Color(0xFFFFC107),
			Color(0xFFFFCA28)
		)
		
		val Yellow: Color
			@Composable
			get() = yellow.current
		
		private val red = NoColor(
			Color(0xFFF44336),
			Color(0xFFE57373)
		)
		
		val Red: Color
			@Composable
			get() = red.current
		
		private val gray = NoColor(
			Color(0xFF9E9E9E),
			Color(0xFFBDBDBD)
		)
		
		val Gray: Color
			@Composable
			get() = gray.current
	}
}