package com.nocircle.script

fun main() {
//    val code = generateColorSchemeGroupCodes("/Users/cooder/Downloads/material-theme-41/ui/theme/Color.kt", "BlueGrey")
//    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
//    val selection = StringSelection(code)
//    clipboard.setContents(selection, null)
//    println("代码生成完毕，已复制！")
    val regex = """^(.+?)\s*:\s*(.+)$""".toRegex()
    val values = regex.matchEntire("hello : world")!!.groupValues
//    format(values[1].trim()) to values[2].trim()
    println(values[1].trim())
    println(values[2].trim())
}