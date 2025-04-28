package com.nocircle.compose.expends

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

val StringResource.value: String
	@Composable
	get() = stringResource(this)

suspend fun StringResource.value(): String {
	return getString(this)
}