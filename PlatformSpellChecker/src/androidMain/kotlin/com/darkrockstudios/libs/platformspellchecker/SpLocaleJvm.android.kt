package com.darkrockstudios.libs.platformspellchecker

import android.os.Build

/** JVM-only convenience conversions between [SpLocale] and [java.util.Locale] (Android/JVM).
 */
fun SpLocale.toJavaLocale(): java.util.Locale {
	return javaLocaleOf(language, country?.takeIf { it.isNotBlank() }.orEmpty())
}

fun java.util.Locale.toSpLocale(): SpLocale {
	val lang = language.ifBlank { "" }
	val ctry = country.ifBlank { null }
	return SpLocale(lang, ctry)
}

// Locale.of only exists from API 36; the constructors it replaces behave identically.
internal fun javaLocaleOf(language: String, country: String): java.util.Locale {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
		java.util.Locale.of(language, country)
	} else {
		@Suppress("DEPRECATION")
		java.util.Locale(language, country)
	}
}
