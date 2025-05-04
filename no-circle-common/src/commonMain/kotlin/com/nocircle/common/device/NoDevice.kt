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
	
	Desktop
}