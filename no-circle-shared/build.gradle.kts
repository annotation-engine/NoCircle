import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

kotlin {
	jvmToolchain(21)
	
	androidTarget {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_3
			apiVersion = KotlinVersion.KOTLIN_2_3
		}
	}
	
	listOf(
		iosArm64(),
		iosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "NoCircleShared"
			isStatic = true
			linkerOpts += "-lsqlite3"
		}
	}
	
	jvm("desktop") {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_3
			apiVersion = KotlinVersion.KOTLIN_2_3
		}
	}
	
	sourceSets {
		commonMain.dependencies {
			implementation(libs.bundles.shared)
		}
	}
	
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_3
		apiVersion = KotlinVersion.KOTLIN_2_3
		freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xcontext-parameters")
	}
}

android {
	namespace = "com.nocircle.shared"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
			merges += "/META-INF/DEPENDENCIES"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_25
		targetCompatibility = JavaVersion.VERSION_25
	}
}