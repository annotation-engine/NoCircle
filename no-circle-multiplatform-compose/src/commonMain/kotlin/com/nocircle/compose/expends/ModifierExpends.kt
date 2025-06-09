package com.nocircle.compose.expends

import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset

fun Modifier.offset(offset: DpOffset): Modifier =
	this.offset(offset.x, offset.y)