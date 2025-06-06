package com.nocircle.app.pages.settings.appearance

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.unit.Dp
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AppearanceViewModel : NoViewModel() {
	
	val colorSchemeCardWidth = MutableStateFlow(Dp.Unspecified)
	
	val themeModeSystemPercent = Animatable(0f)
	
	val isDarkPreview = MutableStateFlow(false)
}