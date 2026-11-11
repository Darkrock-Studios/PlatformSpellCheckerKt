package com.darkrockstudios.libs.platformspellchecker

/**
 * Linux (Kotlin/Native) implementation, with no spell-checking backend yet.
 *
 * It knows no language, so it flags nothing: every word checks as correct. The app-local user
 * dictionary still works, so callers can keep and read back their words. [PlatformSpellCheckerFactory]
 * reports no languages, so a caller that checks [PlatformSpellCheckerFactory.hasLanguage] first never
 * gets one of these; only `createSpellChecker(null)` does. [DictionaryScope.System] falls back to
 * [DictionaryScope.AppLocal], as on Android.
 *
 * The JVM desktop target on Linux uses Hunspell; this target does not.
 */
actual class PlatformSpellChecker internal constructor() {
	private val userDict = UserDictionary()

	actual suspend fun checkMultiword(text: String): List<SpellingCorrection> = emptyList()

	actual suspend fun checkWord(word: String, maxSuggestions: Int): WordCheckResult = CorrectWord(word)

	actual suspend fun isWordCorrect(word: String): Boolean = true

	actual suspend fun addToDictionary(word: String, scope: DictionaryScope) = userDict.add(word)

	actual suspend fun removeFromDictionary(word: String, scope: DictionaryScope) = userDict.remove(word)

	actual suspend fun ignoreWord(word: String) = userDict.ignore(word)

	actual suspend fun setUserDictionary(words: Collection<String>) = userDict.replace(words)

	actual fun userDictionary(): Set<String> = userDict.snapshot()

	actual fun close() {}
}
