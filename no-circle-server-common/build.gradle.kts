import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
}

kotlin {
	jvmToolchain(21)
	
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_1
		jvmTarget = JvmTarget.JVM_21
		freeCompilerArgs.add("-Xcontext-parameters")
	}
}

dependencies {
	implementation(libs.bundles.server.common)
}