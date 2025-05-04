package com.nocircle.server.plugins

import com.nocircle.server.utils.NoLog
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.utils.io.*
import org.slf4j.event.Level

@OptIn(InternalAPI::class)
fun Application.configureLogging() {
	install(CallLogging) {
		level = Level.INFO
		logger = NoLog.NoCircleLogger
		filter { it.request.uri.startsWith("/api") }
	}
	install(ResponseLogging)
}

private val ResponseLogging = createApplicationPlugin("NoLogging") {
	onCallRespond { _, value ->
		val message = when (value) {
			is TextContent -> "[Text] - ${value.contentLength} length\n${value.text}"
			is OutgoingContent.ByteArrayContent -> "[ByteArray] - ${value.contentLength} length - ${value.bytes().toString(Charsets.UTF_8)}"
			is OutgoingContent.ReadChannelContent -> "[ReadChannel] - ${value.contentLength} length"
			is String -> "[String] - ${value.length} - $value"
			else -> return@onCallRespond
		}
		NoLog.info("Response $message")
	}
}