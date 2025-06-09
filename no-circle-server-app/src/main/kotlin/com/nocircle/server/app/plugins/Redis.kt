package com.nocircle.server.app.plugins

import com.nocircle.server.common.log.NoLog
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import kotlin.system.measureTimeMillis

fun configureRedis() {
	val millis = measureTimeMillis {
		val config = Config().apply {
			useSingleServer().apply {
				address = yaml.redis.address
				database = yaml.redis.database
			}
		}
		redisson = Redisson.create(config)
	}
	NoLog.info("Redis connected used for ${millis / 1_000f} seconds.")
}

lateinit var redisson: RedissonClient

abstract class NoRedisson {
	
	val key: String by lazy {
		this::class.qualifiedName!!.replace(".", "::")
	}
	
	val prefix: String by lazy {
		"${this.key}::"
	}
}

data object UserToken : NoRedisson()