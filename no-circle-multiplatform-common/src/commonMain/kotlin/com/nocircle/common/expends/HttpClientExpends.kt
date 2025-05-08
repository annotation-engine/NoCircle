package com.nocircle.common.expends

import com.nocircle.common.config.getConfigOrNull
import com.nocircle.common.log.NoLog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable

suspend inline fun <reified R : Any> HttpClient.safeGet(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Get, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePost(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Post, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePut(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Put, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeDelete(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Delete, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePatch(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Patch, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeHead(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Head, auth, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeOptions(
	urlString: String,
	auth: Boolean = true,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Options, auth, typeInfo<ApiResult<R>>(), block)

suspend fun <R : Any> HttpClient.safeRequest(
	urlString: String,
	method: HttpMethod,
	auth: Boolean,
	typeInfo: TypeInfo,
	block: HttpRequestBuilder.() -> Unit
): ApiResult<R>? = try {
	val builder = HttpRequestBuilder().apply(block)
	builder.url(urlString)
	builder.method = method
	if (auth) {
		getConfigOrNull<String>("token")?.let {
			builder.bearerAuth(it)
		}
	}
	val response = this.request(builder)
	if (response.status.isSuccess()) {
		response.body(typeInfo)
	} else null
} catch (e: CancellationException) {
	throw e
} catch (e: Exception) {
	NoLog.error(e)
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