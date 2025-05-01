package com.nocircle.server.plugins

import com.nocircle.server.utils.LogUtils
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
	LogUtils.info("Redis connected used for ${millis / 1_000f} seconds.")
}

lateinit var redisson: RedissonClient

data object RedisPrefix {
	
	const val USER_TOKEN = "user::token::"
}