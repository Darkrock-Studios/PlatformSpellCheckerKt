plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.compose)
}

android {
	namespace = "com.darkrockstudios.libs.platformspellchecker.app"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "com.darkrockstudios.libs.platformspellchecker"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.compileSdk.get().toInt()
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	compileOptions {
		val jvmVersion = libs.versions.jvm.get()
		sourceCompatibility = JavaVersion.toVersion(jvmVersion)
		targetCompatibility = JavaVersion.toVersion(jvmVersion)
	}

	buildFeatures {
		compose = true
	}
}

dependencies {
	implementation(project(":exampleApp"))
	implementation(project(":PlatformSpellChecker"))

	implementation(libs.kotlinx.coroutines.android)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.koin.android)
}
