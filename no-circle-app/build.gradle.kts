import org.gradle.kotlin.dsl.support.uppercaseFirstChar
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
}

val noCircleVersionName = property("no-circle.version.name").toString()
val noCircleVersionCode = property("no-circle.version.code").toString().toInt()
val noCircleIOSTargets = property("no-circle.iosTargets").toString().split(",").map {
	it.trim().also {
		check(it in arrayOf("x64", "arm64", "simulatorArm64")) {
			"iosTargets 只允许是：x64, arm64, simulatorArm64 的，多个用 ‘,’ 分割"
		}
	}
}

kotlin {
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
			baseName = "NoCircleApp"
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
			implementation(projects.noCircleCompose)
			implementation(projects.noCircleCommon)
			implementation(compose.preview)
			implementation(libs.androidx.activity.compose)
		}
		commonMain.dependencies {
			implementation(projects.noCircleCompose)
			implementation(projects.noCircleCommon)
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.uiToolingPreview)
			implementation(compose.components.resources)
			implementation(compose.materialIconsExtended)
			implementation(libs.bundles.app)
		}
		desktopMain.dependencies {
			implementation(projects.noCircleCompose)
			implementation(projects.noCircleCommon)
			implementation(compose.desktop.currentOs)
			implementation(libs.kotlinx.coroutines.swing)
		}
		commonMain.configure {
			kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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
		versionCode = noCircleVersionCode
		versionName = noCircleVersionName
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
	add("kspCommonMainMetadata", libs.room.compiler)
	add("kspAndroid", libs.room.compiler)
	add("kspDesktop", libs.room.compiler)
	
	noCircleIOSTargets.forEach {
		add("kspIos${it.uppercaseFirstChar()}", libs.room.compiler)
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
			packageName = "com.nocircle.app"
			packageVersion = noCircleVersionName
		}
	}
}

compose.resources {
	packageOfResClass = "com.nocircle.app.generated.resources"
	publicResClass = false
}