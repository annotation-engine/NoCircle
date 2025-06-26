package com.nocircle.compose.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nocircle.compose.material3.NoWave

@Composable
@NonRestartableComposable
fun NoAsyncImage(
	url: String?,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	transform: (State) -> State = AsyncImagePainter.DefaultTransform,
	loading: @Composable (SubcomposeAsyncImageScope.(State.Loading) -> Unit)? = { NoWave(4) },
	success: @Composable (SubcomposeAsyncImageScope.(State.Success) -> Unit)? = null,
	error: @Composable (SubcomposeAsyncImageScope.(State.Error) -> Unit)? = null,
	onLoading: ((State.Loading) -> Unit)? = null,
	onSuccess: ((State.Success) -> Unit)? = null,
	onError: ((State.Error) -> Unit)? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Crop,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null,
	filterQuality: FilterQuality = DefaultFilterQuality,
	clipToBounds: Boolean = true,
	crossfade: Boolean = true,
	diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
	memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
) {
	SubcomposeAsyncImage(
		model = ImageRequest.Builder(LocalPlatformContext.current)
			.data(url)
			.crossfade(crossfade)
			.diskCachePolicy(diskCachePolicy)
			.memoryCachePolicy(memoryCachePolicy)
			.build(),
		contentDescription = contentDescription,
		modifier = modifier,
		transform = transform,
		loading = loading,
		success = success,
		error = error,
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

@Composable
fun NoAsyncImage(
	request: ImageRequest,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	transform: (State) -> State = AsyncImagePainter.DefaultTransform,
	loading: @Composable (SubcomposeAsyncImageScope.(State.Loading) -> Unit)? = null,
	success: @Composable (SubcomposeAsyncImageScope.(State.Success) -> Unit)? = null,
	error: @Composable (SubcomposeAsyncImageScope.(State.Error) -> Unit)? = null,
	onLoading: ((State.Loading) -> Unit)? = null,
	onSuccess: ((State.Success) -> Unit)? = null,
	onError: ((State.Error) -> Unit)? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Fit,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null,
	filterQuality: FilterQuality = DefaultFilterQuality,
	clipToBounds: Boolean = true,
) {
	SubcomposeAsyncImage(
		model = request,
		contentDescription = contentDescription,
		modifier = modifier,
		transform = transform,
		loading = loading,
		success = success,
		error = error,
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