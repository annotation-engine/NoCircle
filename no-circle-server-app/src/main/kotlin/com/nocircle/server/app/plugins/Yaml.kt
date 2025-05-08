package com.nocircle.server.app.plugins

import com.nocircle.server.common.utils.ConstructorParameterFormat
import com.nocircle.server.common.utils.NoLog
import com.nocircle.server.common.utils.loadYaml
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
	val mysql: MysqlConfig,
	val security: SecurityConfig,
	val redis: RedisConfig,
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

class MysqlConfig private constructor(
	val url: String,
	val user: String,
	val driver: String,
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