package com.nocircle.compose.resources

import com.nocircle.common.resources.NoString

internal enum class ComposeString : NoString {
	NetworkConnectError,
	Confirm,
	Cancel;
	
	override val packageName = "com.nocircle.compose"
}