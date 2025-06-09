import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ktor)
}

group = "com.nocircle.server.app"
version = property("no-circle.version.name").toString()

application {
	mainClass = "io.ktor.server.netty.EngineMain"
	
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

kotlin {
	jvmToolchain(21)
	
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_1
		apiVersion = KotlinVersion.KOTLIN_2_1
		jvmTarget = JvmTarget.JVM_21
	}
}

dependencies {
	implementation(projects.noCircleServerCommon)
	implementation(libs.bundles.server.app)
}