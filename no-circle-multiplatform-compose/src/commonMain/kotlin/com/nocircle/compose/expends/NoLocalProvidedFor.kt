package com.nocircle.compose.expends

fun noLocalProvidedFor(name: String): Nothing {
	error("CompositionLocal $name not present")
}