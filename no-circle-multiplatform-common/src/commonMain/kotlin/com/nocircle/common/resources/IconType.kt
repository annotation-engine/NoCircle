package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig

enum class IconType {
	ROUNDED,
	OUTLINED,
	FILLED,
	SHARP,
	TWO_TONE;
	
	companion object : StatusFlowConfig<IconType>() {
		override suspend fun getConfigFromStorage(): IconType {
			return IconTypeConfigKey.get() ?: ROUNDED
		}
		
		override suspend fun setConfigToStorage(oldConfig: IconType, newConfig: IconType) {
			IconTypeConfigKey.set(newConfig)
		}
	}
}

private object IconTypeConfigKey : ConfigKey<IconType>("iconType")