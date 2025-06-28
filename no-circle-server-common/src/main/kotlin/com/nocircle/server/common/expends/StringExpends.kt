package com.nocircle.server.common.expends

import cn.hutool.extra.pinyin.PinyinUtil

fun String.getDisplayLength(): Int = this.sumOf {
	if (it.isWideChar()) 2 else 1
}

fun String.toPinyin(
	separator: String = " ",
	tone: Boolean = false
): String = PinyinUtil.getPinyin(this, separator, tone)

fun String.isLowerCases(): Boolean {
	return this.all { it.isLowerCase() }
}

fun String.isUpperCases(): Boolean {
	return this.all { it.isUpperCase() }
}

fun String.isDigits(): Boolean {
	return this.all { it.isDigit() }
}