package com.nocircle.common.device

actual object NoDevice {
	
	actual val name by lazy {
		val osName = System.getProperty("os.name").lowercase()
		when {
			osName.contains("win") -> DeviceName.Windows
			osName.contains("mac") -> DeviceName.MacOS
			osName.contains("nux") -> DeviceName.Linux
			else -> DeviceName.Unknown
		}
	}
	
	actual val type = DeviceType.Desktop
}