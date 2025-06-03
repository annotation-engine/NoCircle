package com.nocircle.common.device

expect object NoDevice {
	
	val Name: DeviceName
	
	val Type: DeviceType
}

enum class DeviceName {
	
	Android,
	
	IOS,
	
	MacOS,
	
	Linux,
	
	Windows,
	
	Unknown
}

enum class DeviceType {
	
	Mobile,
	
	Desktop;
	
	companion object {
		
		val isMobile by lazy { NoDevice.Type == Mobile }
		
		val isDesktop by lazy { NoDevice.Type == Desktop }
	}
}