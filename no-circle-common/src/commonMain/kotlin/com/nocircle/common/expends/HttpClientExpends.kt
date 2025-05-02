package com.nocircle.common.expends

import com.nocircle.common.log.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable

suspend inline fun <reified R : Any> HttpClient.safeGet(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Get, block)

suspend inline fun <reified R : Any> HttpClient.safePost(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Post, block)

suspend inline fun <reified R : Any> HttpClient.safePut(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Put, block)

suspend inline fun <reified R : Any> HttpClient.safeDelete(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R>? = this.safeRequest(urlString, HttpMethod.Delete, block)

suspend inline fun <reified R : Any> HttpClient.safeRequest(
	urlString: String,
	method: HttpMethod,
	block: HttpRequestBuilder.() -> Unit
): ApiResult<R>? = try {
	val builder = HttpRequestBuilder().apply(block)
	builder.url(urlString)
	builder.method = method
	val response = this.request(builder)
	if (response.status.isSuccess()) {
		response.body()
	} else null
} catch (e: CancellationException) {
	throw e
} catch (e: Exception) {
	Log.error(e)
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