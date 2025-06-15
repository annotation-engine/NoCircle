package com.nocircle.common.expends

fun noLocalProvidedFor(name: String): Nothing {
	error("CompositionLocal $name not present")
}