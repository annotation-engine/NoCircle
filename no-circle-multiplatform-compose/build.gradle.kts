import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.multiplatform)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.hot.reload)
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
			baseName = "NoCircleCompose"
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
		val desktopMain by getting
		
		androidMain.dependencies {
			implementation(libs.bundles.multiplatform.compose.android)
		}
		commonMain.dependencies {
			implementation(libs.bundles.multiplatform.compose)
			implementation(projects.noCircleMultiplatformCommon)
		}
		sourceSets.commonMain {
			kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
		}
		desktopMain.dependencies {
			implementation(compose.desktop.currentOs)
			implementation(libs.bundles.multiplatform.compose.desktop)
		}
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_3
		apiVersion = KotlinVersion.KOTLIN_2_3
		freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xcontext-parameters")
	}
}

android {
	namespace = "com.nocircle.compose"
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
	buildFeatures {
		compose = true
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.compose.generated.resources"
	publicResClass = false
}