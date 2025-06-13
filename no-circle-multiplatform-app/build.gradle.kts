import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.compose.multiplatform)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.room)
	alias(libs.plugins.hot.reload)
}

val noCircleVersionName = property("no-circle.version.name").toString()
val noCircleVersionCode = property("no-circle.version.code").toString().toInt()

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
			baseName = "NoCircleApp"
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
			implementation(projects.noCircleMultiplatformCompose)
			implementation(projects.noCircleMultiplatformCommon)
			implementation(compose.preview)
			implementation(libs.bundles.multiplatform.app.android)
		}
		commonMain.dependencies {
			implementation(projects.noCircleMultiplatformCompose)
			implementation(projects.noCircleMultiplatformCommon)
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.materialIconsExtended)
			implementation(libs.bundles.multiplatform.app)
		}
		desktopMain.dependencies {
			implementation(projects.noCircleMultiplatformCompose)
			implementation(projects.noCircleMultiplatformCommon)
			implementation(compose.desktop.currentOs)
			implementation(libs.bundles.multiplatform.app.desktop)
		}
		iosMain.dependencies {
			implementation(projects.noCircleMultiplatformCompose)
			implementation(projects.noCircleMultiplatformCommon)
			implementation(libs.bundles.multiplatform.app.ios)
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
	namespace = "com.nocircle.app"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		applicationId = "com.nocircle.app"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = noCircleVersionCode
		versionName = noCircleVersionName
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
	kspCommonMainMetadata(libs.ktorfitx.ksp)
	add("kspAndroid", libs.room.compiler)
	add("kspDesktop", libs.room.compiler)
	add("kspIosX64", libs.room.compiler)
	add("kspIosArm64", libs.room.compiler)
	add("kspIosSimulatorArm64", libs.room.compiler)
}

afterEvaluate {
	
	val taskNames = listOf(
		"kspDebugKotlinAndroid",
		"kspReleaseKotlinAndroid",
		"kspKotlinDesktop",
		"kspKotlinIosX64",
		"kspKotlinIosArm64",
		"kspKotlinIosSimulatorArm64",
	)
	taskNames.forEach {
		tasks.named(it) {
			dependsOn("kspCommonMainKotlinMetadata")
		}
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

compose.desktop {
	application {
		mainClass = "com.nocircle.app.MainKt"
		
		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "NoCircle"
			packageVersion = noCircleVersionName
			description = "No Circle App"
			copyright = "© 2025 NoCircle. All rights reserved."
			vendor = "NoCircle"
		}
		buildTypes.release.proguard {
			configurationFiles.from(project.file("proguard-rules.pro"))
			obfuscate = true
			optimize = true
		}
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.app.generated.resources"
	publicResClass = false
}