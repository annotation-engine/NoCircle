package com.nocircle.server.app

import com.nocircle.server.app.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) {
	EngineMain.main(args)
}

fun Application.module() {
	configureYaml()
	configureJson()
	configureMysql()
	configureRedis()
	configureSecurity()
	configureLogging()
	configureStatusPages()
	configureServices()
}