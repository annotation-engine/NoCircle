package com.nocircle.server.app.plugins

import com.nocircle.server.common.log.ConstructorParameterFormat
import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.log.loadYaml
import io.ktor.server.application.*
import kotlin.system.measureTimeMillis
import kotlin.time.Duration

fun Application.configureYaml() {
	val millis = measureTimeMillis {
		yaml = environment.config.loadYaml<YamlConfig>()
	}
	NoLog.info("Yaml loaded used for ${millis / 1_000f} seconds.")
}

lateinit var yaml: YamlConfig

class YamlConfig private constructor(
	val jwt: JwtConfig,
	val database: DatabaseConfig,
	val security: SecurityConfig,
	val redis: RedisConfig,
	val logger: LoggerConfig
)

class JwtConfig private constructor(
	val issuer: String,
	val audience: String,
	val realm: String,
	val secret: String,
	val timeout: Duration,
) {
	companion object {
		
		@Suppress("unused")
		@ConstructorParameterFormat("timeout")
		fun formatTimeout(value: String): Duration {
			return Duration.parse(value)
		}
	}
}

class DatabaseConfig private constructor(
	val driver: String,
	val host: String,
	val port: Int,
	val name: String,
	val user: String,
	val password: String,
)

class SecurityConfig private constructor(
	val algorithm: String,
	val pepper: String,
	val saltLength: Int,
	val keyLength: Int,
	val iterations: Int,
)

class RedisConfig private constructor(
	val address: String,
	val database: Int,
)

class LoggerConfig private constructor(
	val enabled: Boolean,
	val responseBody: Boolean
)