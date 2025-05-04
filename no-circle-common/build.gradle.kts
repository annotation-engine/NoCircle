import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.multiplatform)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.room)
}

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
			baseName = "NoCircleCommon"
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
			implementation(libs.bundles.kotlin.multiplatform)
		}
		sourceSets.commonMain {
			kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
		}
		desktopMain.dependencies {
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

compose.resources {
	packageOfResClass = "com.nocircle.common.generated.resources"
	publicResClass = false
}