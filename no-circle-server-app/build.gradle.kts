import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ktor)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfitx.server)
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
		languageVersion = KotlinVersion.KOTLIN_2_3
		apiVersion = KotlinVersion.KOTLIN_2_3
		jvmTarget = JvmTarget.JVM_21
		freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xcontext-parameters")
	}
}

dependencies {
	implementation(projects.noCircleServerCommon)
	implementation(projects.noCircleShared)
	implementation(libs.bundles.server.app)
}

ktorfitx {
	websockets.enabled = true
	auth.enabled = true
}