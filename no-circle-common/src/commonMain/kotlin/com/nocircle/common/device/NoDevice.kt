package com.nocircle.common.device

expect object NoDevice {
	
	val name: DeviceName
	
	val type: DeviceType
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
	
	Desktop
}