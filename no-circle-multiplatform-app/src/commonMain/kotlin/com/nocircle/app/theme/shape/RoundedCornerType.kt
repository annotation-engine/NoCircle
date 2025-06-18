package com.nocircle.app.theme.shape

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StatusFlowConfig

enum class RoundedCornerType(
	val extraSmall: Dp,
	val small: Dp,
	val medium: Dp,
	val large: Dp,
	val extraLarge: Dp
) {
	EXTRA_SMALL(
		extraSmall = 1.dp,
		small = 2.dp,
		medium = 3.dp,
		large = 4.dp,
		extraLarge = 7.dp
	),
	SMALL(
		extraSmall = 2.dp,
		small = 4.dp,
		medium = 6.dp,
		large = 8.dp,
		extraLarge = 14.dp
	),
	MEDIUM(
		extraSmall = 4.dp,
		small = 8.dp,
		medium = 12.dp,
		large = 16.dp,
		extraLarge = 28.dp
	),
	LARGE(
		extraSmall = 6.dp,
		small = 12.dp,
		medium = 18.dp,
		large = 24.dp,
		extraLarge = 42.dp
	),
	EXTRA_LARGE(
		extraSmall = 8.dp,
		small = 16.dp,
		medium = 24.dp,
		large = 32.dp,
		extraLarge = 56.dp
	);
	
	companion object Companion : StatusFlowConfig<RoundedCornerType>() {
		
		override suspend fun getConfigFromStorage(): RoundedCornerType {
			return RoundedCornerTypeConfigKey.get() ?: MEDIUM
		}
		
		override suspend fun setConfigToStorage(oldConfig: RoundedCornerType, newConfig: RoundedCornerType) {
			RoundedCornerTypeConfigKey.set(newConfig)
		}
	}
}

private object RoundedCornerTypeConfigKey : ConfigKey<RoundedCornerType>("roundedCornerType")