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
		_redisson = Redisson.create(config)
	}
	NoLog.info("Redis connected used for ${millis / 1_000f} seconds.")
}

private var _redisson: RedissonClient? = null

val redisson get() = _redisson!!

object NoRedisKey {
	
	const val USER_TOKEN = "user::token"
}