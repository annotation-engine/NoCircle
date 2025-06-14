package com.nocircle.server.app.plugins

import com.nocircle.server.common.plugins.NoLogging
import io.ktor.server.application.*

fun Application.configureLogging() {
	val logger = yaml.logger
	if (logger.enabled) {
		install(NoLogging) {
			this.responseBody = logger.responseBody
		}
	}
}