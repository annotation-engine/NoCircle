plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.compose.multiplatform) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.kotlin.multiplatform) apply false
	alias(libs.plugins.kotlin.serialization) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.ktor) apply false
	alias(libs.plugins.ksp) apply false
	alias(libs.plugins.room) apply false
	alias(libs.plugins.hot.reload) apply false
	alias(libs.plugins.ktorfitx.multiplatform) apply false
	alias(libs.plugins.ktorfitx.server) apply false
}