package com.darkrockstudios.libs.platformspellchecker

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Linux (Kotlin/Native) factory. There is no spell-checking backend on this target yet, so it reports
 * no languages and [isAvailable] is false. See [PlatformSpellChecker] for what a checker does here.
 */
actual class PlatformSpellCheckerFactory {
	/** @throws IllegalArgumentException for any non-null [locale], since none is supported. */
	actual suspend fun createSpellChecker(locale: SpLocale?): PlatformSpellChecker {
		require(locale == null || hasLanguage(locale)) {
			"Locale not supported: ${locale?.language}${locale?.country?.let { "_$it" } ?: ""}"
		}
		return PlatformSpellChecker()
	}

	actual fun hasLanguage(locale: SpLocale): Boolean = false

	actual fun isAvailable(): Boolean = false

	/** From the POSIX locale variables, in the order the C library reads them; English when none is set. */
	actual fun currentSystemLocale(): SpLocale =
		LOCALE_VARIABLES.firstNotNullOfOrNull { posixLocaleToSpLocale(env(it)) } ?: SpLocale.EN

	actual fun availableLocales(): List<SpLocale> = emptyList()

	@OptIn(ExperimentalForeignApi::class)
	private fun env(name: String): String? = getenv(name)?.toKString()

	private companion object {
		val LOCALE_VARIABLES = listOf("LC_ALL", "LC_MESSAGES", "LANG")
	}
}

/**
 * A POSIX locale name such as `en_US.UTF-8` or `de_DE@euro` as an [SpLocale], or null for an unset,
 * empty, `C` or `POSIX` locale.
 */
internal fun posixLocaleToSpLocale(value: String?): SpLocale? {
	val name = value?.substringBefore('.')?.substringBefore('@')?.trim()
	if (name.isNullOrEmpty() || name == "C" || name == "POSIX") return null
	val language = name.substringBefore('_').lowercase()
	val country = name.substringAfter('_', "").uppercase().ifEmpty { null }
	return SpLocale(language, country)
}
