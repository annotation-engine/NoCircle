package com.nocircle.script

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

fun main() {
	val code = generateColorSchemeGroupCodes("/Users/cooder/Downloads/material-theme-41/ui/theme/Color.kt", "BlueGrey")
	val clipboard = Toolkit.getDefaultToolkit().systemClipboard
	val selection = StringSelection(code)
	clipboard.setContents(selection, null)
	println("代码生成完毕，已复制！")
}