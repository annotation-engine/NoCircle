package com.nocircle.compose.foundation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

@Composable
@NonRestartableComposable
fun NoImage(
	bitmap: ImageBitmap,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Crop,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null,
	filterQuality: FilterQuality = DrawScope.DefaultFilterQuality
) {
	Image(
		bitmap = bitmap,
		contentDescription = contentDescription,
		modifier = modifier,
		alignment = alignment,
		contentScale = contentScale,
		alpha = alpha,
		colorFilter = colorFilter,
		filterQuality = filterQuality
	)
}

@Composable
@NonRestartableComposable
fun NoImage(
	imageVector: ImageVector,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Crop,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null
) {
	Image(
		imageVector = imageVector,
		contentDescription = contentDescription,
		modifier = modifier,
		alignment = alignment,
		contentScale = contentScale,
		alpha = alpha,
		colorFilter = colorFilter,
	)
}

@Composable
fun NoImage(
	painter: Painter,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Crop,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null
) {
	Image(
		painter = painter,
		contentDescription = contentDescription,
		modifier = modifier,
		alignment = alignment,
		contentScale = contentScale,
		alpha = alpha,
		colorFilter = colorFilter,
	)
}