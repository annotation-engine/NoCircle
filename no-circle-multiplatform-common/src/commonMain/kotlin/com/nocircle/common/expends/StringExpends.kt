package com.nocircle.common.expends

fun String.isAlphanumeric(): Boolean {
    return this.all { it.isLetterOrDigit() }
}

fun String.isNotAlphanumeric(): Boolean {
    return this.any { !(it.isLetterOrDigit()) }
}

fun String.getDisplayLength(): Int = this.sumOf {
    if (it.isWideChar()) TWO else ONE
}

private const val ONE = 1
private const val TWO = 2

private val formatRegex = """\{(\d*)\}""".toRegex()

fun String.format(vararg args: Any?): String {
    if (args.isEmpty()) return this
    var autoIndex = 0
    return this.replace(formatRegex) { match ->
        val group = match.groupValues[1]
        autoIndex++
        val index = if (group.isEmpty()) autoIndex else group.toIntOrNull() ?: return@replace match.value
        if (index in 1..args.size) {
            args[index - 1].toString()
        } else {
            match.value // 保留原样
        }
    }
}