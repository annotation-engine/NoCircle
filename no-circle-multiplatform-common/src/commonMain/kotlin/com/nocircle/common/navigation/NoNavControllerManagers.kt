package com.nocircle.common.navigation

import androidx.compose.runtime.Composable
import com.nocircle.common.windowsize.WindowWidthSizes

object NoNavControllerManager {
	
	private val controllers = mutableMapOf<NoNavHostKey, NoNavHostController>()
	
	@Composable
	fun get(
		moreNavHost: NoNavHostKey = RootNavHost
	): NoNavHostController {
		val navHost = if (WindowWidthSizes.isCompact) RootNavHost else moreNavHost
		return controllers.getOrPut(navHost) {
			rememberNoNavController()
		}
	}
	
	internal fun findKey(controller: NoNavHostController): NoNavHostKey? {
		return controllers.entries.find { it.value == controller }?.key
	}
	
	internal fun removeAllExpectForRoot() {
		controllers.keys.filter { it != RootNavHost }.forEach {
			controllers.remove(it)
		}
	}
}

interface NoNavHostKey

data object RootNavHost : NoNavHostKey