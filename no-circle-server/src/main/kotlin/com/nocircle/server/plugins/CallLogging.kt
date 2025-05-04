package com.nocircle.server.plugins

import com.nocircle.server.utils.NoLog
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureCallLogging() {
	install(CallLogging) {
		level = Level.INFO
		filter { _ -> true }
	}
	install(ResponseLogging)
}

private val ResponseLogging = createApplicationPlugin("LoggingResponsePlugin") {
	onCallRespond { _, value ->
		NoLog.info {
			when (value) {
				is TextContent -> "Response [Text] - ${value.contentLength} length - ${value.text}"
				is OutgoingContent.ByteArrayContent -> "Response [ByteArray] - ${value.contentLength} length - ${value.bytes().toString(Charsets.UTF_8)}"
				is OutgoingContent.ReadChannelContent -> "Response [ReadChannel] - ${value.contentLength} length"
				is String -> "Response [String] - ${value.length} - $value"
				else -> "Response [${value::class.simpleName ?: "Unknown"}] - $value"
			}
		}
	}
}