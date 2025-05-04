package com.nocircle.common.expends

import androidx.lifecycle.viewModelScope
import com.nocircle.common.generated.resources.Res
import com.nocircle.common.generated.resources.common_network_error
import com.nocircle.common.log.Log
import com.nocircle.common.material3.NoSnackbarColors
import com.nocircle.common.viewmodel.NoViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safeGet(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Get, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safePost(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Post, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safePut(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Put, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safeDelete(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Delete, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safePatch(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Patch, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safeHead(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Head, typeInfo<ApiResult<R>>(), block)

context(_: NoViewModel)
suspend inline fun <reified R : Any> HttpClient.safeOptions(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Options, typeInfo<ApiResult<R>>(), block)

context(viewModel: NoViewModel)
suspend fun <R : Any> HttpClient.safeRequest(
	urlString: String,
	method: HttpMethod,
	typeInfo: TypeInfo,
	block: HttpRequestBuilder.() -> Unit
): ApiResult<R>? = try {
	val builder = HttpRequestBuilder().apply(block)
	builder.url(urlString)
	builder.method = method
	val response = this.request(builder)
	if (response.status.isSuccess()) {
		response.body<ApiResult<R>>(typeInfo).also {
			if (it.failure) {
				viewModel.viewModelScope.launch {
					viewModel.showNoSnackbar(it.msg, colors = NoSnackbarColors.Error)
				}
			}
		}
	} else {
		viewModel.showNoSnackbar(Res.string.common_network_error, colors = NoSnackbarColors.Error)
		null
	}
} catch (e: CancellationException) {
	throw e
} catch (e: Exception) {
	Log.error(e)
	viewModel.showNoSnackbar(Res.string.common_network_error, colors = NoSnackbarColors.Error)
	null
}

@Serializable
data class ApiResult<T : Any>(
	val code: Int,
	val msg: String,
	val data: T? = null,
) {
	
	val success by lazy { this.code == 0 }
	
	val failure by lazy { this.code != 0 }
}