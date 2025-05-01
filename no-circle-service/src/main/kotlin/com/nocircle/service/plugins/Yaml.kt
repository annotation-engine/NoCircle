package com.nocircle.service.plugins

import com.nocircle.service.utils.Log
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.system.measureTimeMillis
import kotlin.time.Duration

fun Application.configureYaml() {
	val millis = measureTimeMillis {
		yaml = environment.config.loadYaml<YamlConfig>()
	}
	Log.info("Yaml loaded used for ${millis / 1_000f} seconds.")
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

private inline fun <reified T : Any> ApplicationConfig.loadYaml(): T {
	return analysis(T::class) ?: error("application.yaml 解析失败")
}

private fun <T : Any> ApplicationConfig.analysis(kClass: KClass<T>, pathPrefix: String? = null): T? {
	val constructor = kClass.primaryConstructor ?: return null
	constructor.isAccessible = true
	val formatParameterNameMaps = kClass.companionObject?.declaredFunctions?.associate {
		it.findAnnotation<ConstructorParameterFormat>()?.name to it
	}?.filterKeys { it != null }
	val companionInstance = kClass.companionObjectInstance
	val args = constructor.parameters.associateWith {
		val path = "${if (pathPrefix != null) "$pathPrefix." else ""}${it.name}"
		if (formatParameterNameMaps != null && companionInstance != null && formatParameterNameMaps.containsKey(it.name)) {
			val value = property(path).getString()
			return@associateWith formatParameterNameMaps[it.name]!!.call(companionInstance, value)
		}
		val clazz = it.type.classifier as? KClass<*> ?: return null
		when (clazz) {
			String::class -> property(path).getString()
			Int::class -> property(path).getString().toInt()
			Long::class -> property(path).getString().toLong()
			Boolean::class -> property(path).getString().toBooleanStrict()
			List::class -> {
				val listKClass = it.type.arguments.first().type?.classifier as? KClass<*> ?: return null
				when (listKClass) {
					String::class -> property(path).getList()
					Int::class -> property(path).getList().map(String::toInt)
					Long::class -> property(path).getList().map(String::toLong)
					Boolean::class -> property(path).getList().map(String::toBooleanStrict)
					else -> null
				}
			}
			
			else -> analysis(clazz, path)
		}
	}
	return constructor.callBy(args)
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
private annotation class ConstructorParameterFormat(val name: String)