package com.nocircle.compose.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun NoAsyncImage(
	url: String?,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	placeholder: Painter? = null,
	error: Painter? = null,
	fallback: Painter? = error,
	onLoading: ((State.Loading) -> Unit)? = null,
	onSuccess: ((State.Success) -> Unit)? = null,
	onError: ((State.Error) -> Unit)? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Fit,
	alpha: Float = NoAsyncImageDefaults.DEFAULT_ALPHA,
	colorFilter: ColorFilter? = null,
	filterQuality: FilterQuality = DefaultFilterQuality,
	clipToBounds: Boolean = true,
) {
	AsyncImage(
		model = ImageRequest.Builder(LocalPlatformContext.current)
			.data(url)
			.crossfade(true)
			.build(),
		contentDescription = contentDescription,
		imageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
		modifier = modifier,
		placeholder = placeholder,
		error = error,
		fallback = fallback,
		onLoading = onLoading,
		onSuccess = onSuccess,
		onError = onError,
		alignment = alignment,
		contentScale = contentScale,
		alpha = alpha,
		colorFilter = colorFilter,
		filterQuality = filterQuality,
		clipToBounds = clipToBounds,
	)
}

object NoAsyncImageDefaults {
	
	const val DEFAULT_ALPHA: Float = 1.0f
}