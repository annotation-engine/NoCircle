import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.room)
}

kotlin {
	jvmToolchain(21)
	
	androidTarget {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_2
			apiVersion = KotlinVersion.KOTLIN_2_2
		}
	}
	
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "NoCircleCommon"
			isStatic = true
			linkerOpts += "-lsqlite3"
		}
	}
	
	jvm("desktop") {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_2
			apiVersion = KotlinVersion.KOTLIN_2_2
		}
	}
	
	sourceSets {
		val desktopMain by getting
		
		androidMain.dependencies {
			implementation(libs.bundles.multiplatform.common.android)
		}
		commonMain.dependencies {
			implementation(projects.noCircleShared)
			implementation(libs.bundles.multiplatform.common)
		}
		desktopMain.dependencies {
			implementation(libs.bundles.multiplatform.common.desktop)
		}
	}
	sourceSets.commonMain {
		kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_2
		apiVersion = KotlinVersion.KOTLIN_2_2
		freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xcontext-parameters")
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
			merges += "/META-INF/DEPENDENCIES"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
}

dependencies {
	add("kspAndroid", libs.room.compiler)
	add("kspDesktop", libs.room.compiler)
	add("kspIosX64", libs.room.compiler)
	add("kspIosArm64", libs.room.compiler)
	add("kspIosSimulatorArm64", libs.room.compiler)
}

room {
	schemaDirectory("$projectDir/schemas")
}