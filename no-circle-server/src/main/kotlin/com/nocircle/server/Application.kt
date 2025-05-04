package com.nocircle.server

import com.nocircle.server.plugins.*
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
	configureCallLogging()
	configureStatusPages()
	configureServices()
}