package com.nocircle.compose.resources

import com.nocircle.common.resources.NoString

enum class ComposeString : NoString {
	NETWORK_CONNECT_ERROR,
	CONFIRM,
	CANCEL;
	
	override val packageName = "com.nocircle.compose"
}