package com.nocircle.server.app.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nocircle.server.app.plugins.NoRedisKey
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration
import kotlin.time.toJavaInstant

object JWTUtils {
	
	val algorithm by lazy {
		Algorithm.HMAC256(yaml.jwt.secret)!!
	}
	
	@OptIn(ExperimentalTime::class)
	fun generate(userId: Int, username: String): String {
		val issuedAt = Clock.System.now()
		val expiresAt = issuedAt + yaml.jwt.timeout
		val token = JWT.create()
			.withIssuer(yaml.jwt.issuer)
			.withAudience(yaml.jwt.audience)
			.withClaim("userId", userId)
			.withClaim("username", username)
			.withIssuedAt(issuedAt.toJavaInstant())
			.withExpiresAt(expiresAt.toJavaInstant())
			.sign(algorithm)
		
		redisson.getBucket<String>("${NoRedisKey.USER_TOKEN}::$userId")
			.set(token, yaml.jwt.timeout.toJavaDuration())
		
		return token
	}
}