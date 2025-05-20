package com.nocircle.compose.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.value(
	vararg formatArgs: Any
): String = stringResource(this, *formatArgs)

suspend fun StringResource.string(
	vararg formatArgs: Any,
): String = getString(this, *formatArgs)