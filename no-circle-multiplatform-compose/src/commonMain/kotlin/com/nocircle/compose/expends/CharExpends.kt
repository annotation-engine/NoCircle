package com.nocircle.compose.expends

import androidx.compose.runtime.Stable

/**
 * 全角字符的 Unicode 区间列表（通常占两个英文字符宽度）
 */
private val WideCharRanges = listOf(
	// 常用汉字（CJK Unified Ideographs）
	0x4E00..0x9FFF,
	
	// 汉字扩展区（Ext A～F），含生僻字
	0x3400..0x4DBF,     // 扩展A
	0x20000..0x2A6DF,   // 扩展B
	0x2A700..0x2B73F,   // 扩展C
	0x2B740..0x2B81F,   // 扩展D
	0x2B820..0x2CEAF,   // 扩展E
	0x2CEB0..0x2EBEF,   // 扩展F
	
	// CJK 兼容汉字（含异体字）
	0xF900..0xFAFF,
	
	// CJK 兼容扩展区
	0x2F800..0x2FA1F,
	
	// 中文标点符号（如 、。，？！）
	0x3000..0x303F,
	
	// 日文平假名（如 あいうえお）
	0x3040..0x309F,
	
	// 日文片假名（如 アイウエオ）
	0x30A0..0x30FF,
	
	// 韩文音节（Hangul，如 가나다）
	0xAC00..0xD7AF,
	
	// 韩文 Jamo 字母（单独辅音或元音）
	0x1100..0x11FF,
	
	// 韩文兼容 Jamo（如 ㄱㄴㄷ）
	0x3130..0x318F,
	
	// 全角标点、全角英文字母、货币符号等
	0xFF01..0xFF60,    // 全角 ASCII 标点
	0xFFE0..0xFFE6,    // 全角符号（如 ￥￠）
	
	// Emoji 表情区（常见人脸、动作、图标等）
	0x1F300..0x1F64F,  // 表情符号基本区
	0x1F900..0x1F9FF,  // 补充区
	0x1FA70..0x1FAFF,  // 扩展区（如轮椅、器官等）
	
	// 杂项符号
	0x2100..0x214F,    // 字母符号（如 ™ ℉）
	0x2190..0x21FF,    // 箭头（←→↑↓）
	0x2600..0x26FF,    // 各类符号（☀☎✈等）
	0x2B00..0x2BFF     // 箭头与其他杂项图形
)

@Stable
fun Char.isWideChar(): Boolean {
	val code = this.code
	return WideCharRanges.any { code in it }
}