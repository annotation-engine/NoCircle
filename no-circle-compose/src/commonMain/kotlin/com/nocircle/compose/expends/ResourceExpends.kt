package com.nocircle.compose.expends

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

val StringResource.value: String
	@Composable
	get() = stringResource(this)