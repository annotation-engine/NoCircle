package com.nocircle.common.device

actual object NoDevice {
	
	actual val Name by lazy {
		val osName = System.getProperty("os.name").lowercase()
		when {
			"win" in osName -> DeviceName.WINDOWS
			"mac" in osName -> DeviceName.MACOS
			"nux" in osName -> DeviceName.LINUX
			else -> DeviceName.UNKNOWN
		}
	}
	
	actual val Type = DeviceType.DESKTOP
}