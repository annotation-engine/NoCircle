package com.nocircle.app.pages.settings.memory

import kotlinx.cinterop.*
import platform.darwin.*
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

@OptIn(ExperimentalForeignApi::class)
actual fun getUsedMemory(): Long {
	return memScoped {
		val info = alloc<mach_task_basic_info>()
		val count = alloc<mach_msg_type_number_tVar>()
		count.value = (sizeOf<mach_task_basic_info>() / sizeOf<IntVar>()).toUInt()
		val result = task_info(
			mach_task_self_,
			MACH_TASK_BASIC_INFO.toUInt(),
			info.ptr.reinterpret(),
			count.ptr
		)
		if (result == KERN_SUCCESS) {
			info.resident_size.toLong()
		} else 0L
	}
}

@OptIn(NativeRuntimeApi::class)
actual fun freeMemory() {
	GC.collect()
}