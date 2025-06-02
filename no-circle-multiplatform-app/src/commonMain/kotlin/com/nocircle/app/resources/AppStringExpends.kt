package com.nocircle.app.resources

import com.nocircle.common.resources.loadJsonElementMap

private val packageNames = arrayOf(
    "com.nocircle.app",
    "com.nocircle.compose"
)

suspend fun preloadStringJsonElementMap() {
    packageNames.forEach {
        loadJsonElementMap(it)
    }
}