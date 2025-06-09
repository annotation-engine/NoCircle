package com.nocircle.server.common.expends

inline fun <T, K, V> Iterable<T>.associateWithNotNull(
	transform: (T) -> V?
): Map<K, V> where T : K {
	val result = mutableMapOf<K, V>()
	for (element in this) {
		val value = transform(element)
		if (value != null) {
			result[element] = value
		}
	}
	return result
}