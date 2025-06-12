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
	vararg val weights: Int
) {
	ULTRA_THIN(100, 100, 100, 100, 200, 300, 400, 500, 600),
	EXTRA_THIN(100, 100, 100, 200, 300, 400, 500, 600, 700),
	THIN(100, 100, 200, 300, 400, 500, 600, 700, 800),
	MEDIUM(100, 200, 300, 400, 500, 600, 700, 800, 900),
	BOLD(200, 300, 400, 500, 600, 700, 800, 900, 900),
	EXTRA_BOLD(300, 400, 500, 600, 700, 800, 900, 900, 900),
	ULTRA_BOLD(400, 500, 600, 700, 800, 900, 900, 900, 900), ;
	
	companion object : StatusFlowConfig<FontWeightLevel>() {
		
		override suspend fun getConfigFromStorage(): FontWeightLevel {
			return FontWeightLevelConfigKey.get() ?: MEDIUM
		}
		
		override suspend fun setConfigToStorage(oldConfig: FontWeightLevel, newConfig: FontWeightLevel) {
			FontWeightLevelConfigKey.set(newConfig)
		}
	}
}

private object FontWeightLevelConfigKey : ConfigKey<FontWeightLevel>("fontWeightLevel")