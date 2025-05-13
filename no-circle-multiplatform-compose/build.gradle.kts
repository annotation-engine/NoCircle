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

val noCircleIOSTargets = property("no-circle.iosTargets").toString().split(",").map { targets ->
	targets.trim().also { target ->
		check(target in arrayOf("x64", "arm64", "simulatorArm64")) {
			"iosTargets 只允许是：x64, arm64, simulatorArm64 的，多个用 ‘,’ 分割"
		}
	}
}
kotlin {
	jvmToolchain(21)
	
	androidTarget {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
			languageVersion = KotlinVersion.KOTLIN_2_2
		}
	}
	
	val iosTargets = noCircleIOSTargets.map {
		when (it) {
			"x64" -> iosX64()
			"arm64" -> iosArm64()
			else -> iosSimulatorArm64()
		}
	}
	iosTargets.forEach {
		it.binaries.framework {
			baseName = "NoCircleCompose"
			isStatic = true
			linkerOpts += "-lsqlite3"
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
		languageVersion = KotlinVersion.KOTLIN_2_2
		freeCompilerArgs.addAll("-Xcontext-parameters", "-Xexpect-actual-classes")
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