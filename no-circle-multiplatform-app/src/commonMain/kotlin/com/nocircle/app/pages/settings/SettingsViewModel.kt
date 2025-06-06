package com.nocircle.app.pages.settings

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.config.*
import com.nocircle.common.flow.StatusFlowConfig
import com.nocircle.compose.viewmodel.NoViewModel

class SettingsViewModel : NoViewModel() {
	
	suspend fun logout() {
		ktorfitx.userApi.logout()
		TokenConfigKey.clear()
	}
}

enum class FontWeightLevel(
	val progression: IntProgression
) {
	UltraThin(100..300 step 25),
	ExtraThin(100..500 step 50),
	Thin(100..700 step 75),
	Medium(100..900 step 100),
	Bold(300..900 step 75),
	ExtraBold(500..900 step 50),
	UltraBold(700..900 step 25);
	
	companion object : StatusFlowConfig<FontWeightLevel>() {
		
		override suspend fun getConfigFromStorage(): FontWeightLevel {
			return FontWeightLevelConfigKey.get() ?: Medium
		}
		
		override suspend fun setConfigToStorage(oldConfig: FontWeightLevel, newConfig: FontWeightLevel) {
			FontWeightLevelConfigKey.set(newConfig)
		}
	}
}

private object FontWeightLevelConfigKey : ConfigKey<FontWeightLevel>("fontWeightLevel")