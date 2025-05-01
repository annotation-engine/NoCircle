import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.multiplatform)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_2
		}
	}
	
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "NoCircleCommon"
			isStatic = true
		}
	}
	
	jvm("desktop") {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_2
		}
	}
	
	sourceSets {
		val desktopMain by getting
		
		commonMain.dependencies {
			implementation(compose.runtime)
			implementation(compose.components.resources)
			implementation(libs.androidx.navigation.compose)
		}
		desktopMain.dependencies {
			api(compose.desktop.currentOs)
			api(libs.kotlinx.coroutines.swing)
		}
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_2
		freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
	}
}

android {
	namespace = "com.nocircle.common"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
	buildFeatures {
		compose = true
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.common.generated.resources"
	publicResClass = false
}