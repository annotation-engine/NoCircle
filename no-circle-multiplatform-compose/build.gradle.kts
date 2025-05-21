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
			languageVersion = KotlinVersion.KOTLIN_2_1
		}
	}
	
	listOf(
		iosX64(),
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
			languageVersion = KotlinVersion.KOTLIN_2_1
		}
	}
	
	sourceSets {
		val desktopMain by getting
		
		androidMain.dependencies {
			implementation(projects.noCircleMultiplatformCommon)
			implementation(compose.preview)
			implementation(libs.androidx.activity.compose)
		}
		commonMain.dependencies {
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.uiToolingPreview)
			implementation(compose.components.resources)
			implementation(compose.materialIconsExtended)
			implementation(libs.bundles.multiplatform.compose)
			implementation(projects.noCircleMultiplatformCommon)
		}
		sourceSets.commonMain {
			kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
		}
		desktopMain.dependencies {
			implementation(projects.noCircleMultiplatformCommon)
			implementation(compose.desktop.currentOs)
			implementation(libs.kotlinx.coroutines.swing)
		}
	}
	compilerOptions {
		languageVersion = KotlinVersion.KOTLIN_2_1
		freeCompilerArgs.addAll("-Xexpect-actual-classes")
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

compose.resources {
	packageOfResClass = "com.nocircle.compose.generated.resources"
	publicResClass = false
}