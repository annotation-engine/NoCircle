package com.nocircle.server.app

import com.nocircle.server.app.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
	EngineMain.main(args)
}

fun Application.module() {
	configureYaml()
	configureJson()
	configureDatabase()
	configureRedis()
	configureSecurity()
	configureLogging()
	configureStatusPages()
	configureRoutes()
	configureWebSockets()
}