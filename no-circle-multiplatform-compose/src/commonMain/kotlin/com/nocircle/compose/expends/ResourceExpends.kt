package com.nocircle.compose.expends

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

val DrawableResource.painter: Painter
	@Composable
	get() = painterResource(this)

val DrawableResource.bitmap: ImageBitmap
	@Composable
	get() = imageResource(this)

val DrawableResource.vector: ImageVector
	@Composable
	get() = vectorResource(this)