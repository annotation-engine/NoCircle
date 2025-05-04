package com.nocircle.common.expends

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
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Get, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePost(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Post, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePut(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Put, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeDelete(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Delete, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safePatch(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Patch, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeHead(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Head, typeInfo<ApiResult<R>>(), block)

suspend inline fun <reified R : Any> HttpClient.safeOptions(
	urlString: String,
	noinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Options, typeInfo<ApiResult<R>>(), block)

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