package com.nocircle.service.utils

import cn.hutool.core.util.HexUtil
import cn.hutool.crypto.symmetric.PBKDF2
import com.nocircle.service.plugins.yaml
import io.ktor.util.*
import kotlin.random.Random

object PasswordUtils {
	
	private val pbkdf2 by lazy {
		PBKDF2(
			yaml.security.algorithm,
			yaml.security.keyLength,
			yaml.security.iterations
		)
	}
	
	/**
	 * 加密
	 */
	fun encrypt(plain: String): String {
		val salt = Random.nextBytes(yaml.security.saltLength)
		val pepper = yaml.security.pepper
		val encryptHex = pbkdf2.encryptHex((plain + pepper).toCharArray(), salt)
		val saltHex = HexUtil.encodeHexStr(salt)
		return "$$saltHex$$encryptHex"
	}
	
	/**
	 * 校验
	 */
	fun verity(plain: String, encrypt: String): Boolean {
		val data = encrypt.split('$')
		if (data.size != 3) return false
		val salt = HexUtil.decodeHex(data[1])
		val pepper = yaml.security.pepper
		val encryptHex = pbkdf2.encryptHex((plain + pepper).toCharArray(), salt)
		return encryptHex == data[2]
	}
}