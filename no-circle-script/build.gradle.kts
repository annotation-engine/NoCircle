plugins {
	alias(libs.plugins.kotlin.jvm)
}

group = "com.nocircle.script"
version = property("no-circle.version.name").toString()

dependencies {

}

kotlin {
	jvmToolchain(21)
}