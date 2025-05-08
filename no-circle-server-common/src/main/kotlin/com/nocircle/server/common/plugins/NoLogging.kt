package com.nocircle.server.common.plugins

import com.nocircle.server.common.utils.NoLog
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlin.time.Duration.Companion.milliseconds

private val RequestTimeKey = AttributeKey<Long>("RequestTime")

val NoLogging = createApplicationPlugin(
	name = "NoLogging",
	createConfiguration = ::NoLoggingConfig
) {
	onCall { call ->
		call.attributes.put(RequestTimeKey, System.currentTimeMillis())
	}
	onCallRespond { call, value ->
		val status = call.response.status()
		val httpMethod = call.request.httpMethod
		val uri = call.request.uri
		val duration = System.currentTimeMillis() - call.attributes[RequestTimeKey]
		call.attributes.remove(RequestTimeKey)
		NoLog.buildInfo {
			append(status)
			append(": ")
			append(httpMethod)
			append(" - ")
			append(uri)
			append(" in ${duration.milliseconds} - ")
			if (this@onCallRespond.pluginConfig.responseBody) {
				append("Response: ")
				val message = when (value) {
					is TextContent -> "[Text]\n${value.text}"
					is OutgoingContent.ByteArrayContent -> "[ByteArray]\n${value.bytes().toString(Charsets.UTF_8)}"
					is OutgoingContent.ReadChannelContent -> "[ReadChannel]"
					is String -> "[String]\n$value"
					else -> return@buildInfo
				}
				append(message)
			}
		}
	}
}

data class NoLoggingConfig(
	var responseBody: Boolean = true
)