package com.nocircle.shared.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModuleBuilder

inline fun <reified T : Any> SerializersModuleBuilder.contextual(serializer: KSerializer<T>) {
	contextual(T::class, serializer)
}