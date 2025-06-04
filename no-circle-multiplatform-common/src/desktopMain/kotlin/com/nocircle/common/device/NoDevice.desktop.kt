package com.nocircle.common.device

actual object NoDevice {
	
	actual val Name by lazy {
		val osName = System.getProperty("os.name").lowercase()
		when {
			"win" in osName -> DeviceName.Windows
			"mac" in osName -> DeviceName.MacOS
			"nux" in osName -> DeviceName.Linux
			else -> DeviceName.Unknown
		}
	}
	
	actual val Type = DeviceType.Desktop
}