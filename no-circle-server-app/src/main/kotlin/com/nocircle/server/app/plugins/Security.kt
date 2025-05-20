package com.nocircle.server.app.plugins

import com.auth0.jwt.JWT
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.common.model.NoPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*

fun Application.configureSecurity() {
	authentication {
		jwt {
			realm = yaml.jwt.realm
			verifier(jwtVerifier)
			validate {
				val token = request.token ?: return@validate null
				val userId = it.payload.getClaim("userId").asInt()
				val bucket = redisson.getBucket<String>("${RedisPrefix.USER_TOKEN}$userId")
				if (bucket.isExists && bucket.get() == token) {
					val username = it.payload.getClaim("username").asString()
					NoPrincipal(userId, username)
				} else null
			}
		}
	}
}

private val ApplicationRequest.token: String?
	get() = authorization()?.removePrefix("Bearer ")

private val jwtVerifier by lazy {
	JWT.require(JWTUtils.algorithm)
		.withAudience(yaml.jwt.audience)
		.withIssuer(yaml.jwt.issuer)
		.build()
}