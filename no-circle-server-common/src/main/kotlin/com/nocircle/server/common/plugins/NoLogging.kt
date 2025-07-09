package com.nocircle.server.common.plugins

import com.nocircle.server.common.log.NoLog
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlin.time.Duration.Companion.milliseconds

private val DurationTimeKey = AttributeKey<Long>("DurationTimeKey")

val NoLogging = createApplicationPlugin(
	name = "NoLogging",
	createConfiguration = ::NoLoggingConfig
) {
	onCall { call ->
		call.attributes.put(DurationTimeKey, System.currentTimeMillis())
	}
	onCallRespond { call, value ->
		val status = call.response.status() ?: HttpStatusCode.OK
		val message = buildString {
			append(status)
			append(": ")
			append(call.request.httpMethod)
			append(" - ")
			append(call.request.uri)
			call.attributes.getOrNull(DurationTimeKey)?.let {
				val duration = System.currentTimeMillis() - it
				call.attributes.remove(DurationTimeKey)
				append(" in ${duration.milliseconds}")
			}
			if (this@onCallRespond.pluginConfig.responseBody) {
				append(" - Response: ")
				val responseBody = when (value) {
					is TextContent -> "[Text]\n${value.text}"
					is OutgoingContent.ByteArrayContent -> "[ByteArray]\n${value.bytes().toString(Charsets.UTF_8)}"
					is OutgoingContent.ReadChannelContent -> "[ReadChannel]"
					is String -> "[String]\n$value"
					else -> return@buildString
				}
				append(responseBody)
			}
		}
		NoLog.info(message)
	}
}

data class NoLoggingConfig(
	var responseBody: Boolean = false
)