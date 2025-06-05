package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

enum class IconType {
	Rounded,
	Outlined,
	Filled,
	Sharp,
	TwoTone;
	
	companion object Companion {
		
		val current by lazy { MutableStateFlow(getIconType()) }
		
		private fun getIconType(): IconType = runBlocking(Dispatchers.IO) {
			IconTypeConfigKey.get() ?: Rounded
		}
	}
}

object IconTypeConfigKey : ConfigKey<IconType>