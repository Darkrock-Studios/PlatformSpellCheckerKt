import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.android.kotlin.multiplatform.library)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.compose.multiplatform)
}

kotlin {
	android {
		namespace = "com.darkrockstudios.libs.platformspellchecker.example"
		compileSdk = libs.versions.android.compileSdk.get().toInt()
		minSdk = libs.versions.android.minSdk.get().toInt()

		compilerOptions {
			jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get()))
		}
	}
	jvm("desktop") {
		compilerOptions {
			jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get()))
		}
	}

	// iOS targets
	listOf(
		iosArm64(),
		iosSimulatorArm64()
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "ExampleApp"
			isStatic = true
		}
	}

	sourceSets {
		commonMain.dependencies {
			implementation(project(":PlatformSpellChecker"))

			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)

			implementation(libs.kotlinx.coroutines.core)
			implementation(libs.lifecycle.viewmodel)
			implementation(libs.lifecycle.viewmodel.compose)
			implementation(libs.lifecycle.runtime.compose)

			implementation(libs.koin.core)
			implementation(libs.koin.compose)
			implementation(libs.koin.compose.viewmodel)

			implementation(libs.napier)
		}

		getByName("desktopMain") {
			dependencies {
				implementation(compose.desktop.currentOs)
				implementation(libs.kotlinx.coroutines.swing)
			}
		}
	}
}

compose.desktop {
	application {
		mainClass = "com.darkrockstudios.libs.platformspellchecker.MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "PlatformSpellChecker"
			packageVersion = "1.0.0"

			windows {
				menuGroup = "PlatformSpellChecker"
				upgradeUuid = "5a8c0c3a-1234-5678-90ab-cdef12345678"
			}
		}
	}
}
