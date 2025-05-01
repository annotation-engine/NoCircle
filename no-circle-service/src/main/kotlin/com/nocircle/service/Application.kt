package com.nocircle.service

import com.nocircle.service.plugins.*
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
	configureStatusPages()
	configureServices()
}