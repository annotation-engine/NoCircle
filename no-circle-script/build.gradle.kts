import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.kotlin.jvm)
}

group = "com.nocircle.script"
version = property("no-circle.version.name").toString()

dependencies {

}

kotlin {
	jvmToolchain(21)
	
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_2
		apiVersion = KotlinVersion.KOTLIN_2_2
		freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xcontext-parameters")
	}
}