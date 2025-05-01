import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.compose.multiplatform)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

val noCircleVersion = property("no-circle.version").toString()

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
			baseName = "NoCircleApp"
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
		
		androidMain.dependencies {
		
		}
		commonMain.dependencies {
			implementation(compose.components.resources)
			implementation(projects.noCircleCompose)
			implementation(projects.noCircleCommon)
		}
		desktopMain.dependencies {
		
		}
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_2
		freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
	}
}

android {
	namespace = "com.nocircle.app"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		applicationId = "com.nocircle.app"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 1
		versionName = noCircleVersion
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
			packageVersion = noCircleVersion
		}
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.app.generated.resources"
	publicResClass = false
}