package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig
import kotlinx.serialization.Serializable

@Serializable
enum class NoIconType {
	ROUNDED,
	OUTLINED,
	FILLED,
	SHARP,
	TWO_TONE;
	
	companion object Companion : StatusFlowConfig<NoIconType>() {
		
		override suspend fun getConfigFromStorage(): NoIconType {
			return IconTypeConfigKey.get() ?: ROUNDED
		}
		
		override suspend fun setConfigToStorage(oldConfig: NoIconType, newConfig: NoIconType) {
			IconTypeConfigKey.set(newConfig)
		}
	}
}

private object IconTypeConfigKey : ConfigKey<NoIconType>("iconType")