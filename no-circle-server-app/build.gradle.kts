import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ktor)
}

group = "com.nocircle.service.app"
version = property("no-circle.version.name").toString()

application {
	mainClass = "io.ktor.server.cio.EngineMain"
	
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

kotlin {
	jvmToolchain(21)
	
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_2
		jvmTarget = JvmTarget.JVM_21
		freeCompilerArgs.add("-Xcontext-parameters")
	}
}

dependencies {
	implementation(projects.noCircleServerCommon)
	implementation(libs.bundles.server.app)
}