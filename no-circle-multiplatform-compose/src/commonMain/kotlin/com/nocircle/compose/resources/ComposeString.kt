package com.nocircle.compose.resources

enum class ComposeString : NoString {
	NETWORK_CONNECT_ERROR,
	CONFIRM,
	CANCEL;
	
	override val packageName = "com.nocircle.compose"
}