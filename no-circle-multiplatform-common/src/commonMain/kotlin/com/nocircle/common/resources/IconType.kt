package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig

enum class IconType {
	Rounded,
	Outlined,
	Filled,
	Sharp,
	TwoTone;
	
	companion object : StatusFlowConfig<IconType>() {
		override suspend fun getConfigFromStorage(): IconType {
			return IconTypeConfigKey.get() ?: Rounded
		}
		
		override suspend fun setConfigToStorage(oldConfig: IconType, newConfig: IconType) {
			IconTypeConfigKey.set(newConfig)
		}
	}
}

private object IconTypeConfigKey : ConfigKey<IconType>("iconType")