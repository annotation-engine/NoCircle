package com.nocircle.server.app.plugins

import com.nocircle.server.common.plugins.NoLogging
import io.ktor.server.application.*

fun Application.configureLogging() {
	install(NoLogging) {
		this.responseBody = true
	}
}