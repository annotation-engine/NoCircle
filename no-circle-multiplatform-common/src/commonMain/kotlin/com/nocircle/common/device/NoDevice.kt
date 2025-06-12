package com.nocircle.common.device

expect object NoDevice {
	
	val Name: DeviceName
	
	val Type: DeviceType
}

enum class DeviceName {
	
	ANDROID,
	
	IOS,
	
	MACOS,
	
	LINUX,
	
	WINDOWS,
	
	UNKNOWN
}

enum class DeviceType {
	
	MOBILE,
	
	DESKTOP;
	
	companion object {
		
		val isMobile by lazy { NoDevice.Type == MOBILE }
		
		val isDesktop by lazy { NoDevice.Type == DESKTOP }
	}
}