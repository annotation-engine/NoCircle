import org.jetbrains.compose.desktop.application.dsl.TargetFormat
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
			languageVersion = KotlinVersion.KOTLIN_2_1
		}
	}
	
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "NoCircleApp"
			isStatic = true
		}
	}
	
	jvm("desktop") {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_1
		}
	}
	
	sourceSets {
		val desktopMain by getting
		
		androidMain.dependencies {
			api(compose.preview)
			api(libs.androidx.activity.compose)
		}
		commonMain.dependencies {
			api(compose.runtime)
			api(compose.foundation)
			api(compose.material3)
			api(compose.ui)
			api(compose.components.resources)
			api(compose.components.uiToolingPreview)
			api(compose.materialIconsExtended)
			api(libs.androidx.lifecycle.viewmodel)
			api(libs.androidx.lifecycle.runtime.compose)
			api(libs.navigation.compose)
		}
		desktopMain.dependencies {
			api(compose.desktop.currentOs)
			api(libs.kotlinx.coroutines.swing)
		}
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_1
	}
}

android {
	namespace = "com.nocircle.compose"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
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

dependencies {
	debugImplementation(compose.uiTooling)
}

compose.desktop {
	application {
		mainClass = "com.nocircle.app.MainKt"
		
		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "com.nocircle.app"
			packageVersion = "1.0.0"
		}
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.app.generated.resources"
	publicResClass = true
}