package com.darkrockstudios.libs.platformspellchecker

/** JVM-only convenience conversions between [SpLocale] and [java.util.Locale] (Desktop/JVM).
 */
fun SpLocale.toJavaLocale(): java.util.Locale {
	return javaLocaleOf(language, country?.takeIf { it.isNotBlank() }.orEmpty())
}

fun java.util.Locale.toSpLocale(): SpLocale {
	val lang = language.ifBlank { "" }
	val ctry = country.ifBlank { null }
	return SpLocale(lang, ctry)
}

// Locale.of only exists from JDK 19 and the library targets JDK 17; the constructors it replaces behave identically.
internal fun javaLocaleOf(language: String, country: String): java.util.Locale {
	return if (Runtime.version().feature() >= 19) {
		java.util.Locale.of(language, country)
	} else {
		@Suppress("DEPRECATION")
		java.util.Locale(language, country)
	}
}
