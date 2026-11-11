package com.darkrockstudios.libs.platformspellchecker

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinuxNativeSpellCheckerTest {

	private val factory = PlatformSpellCheckerFactory()

	@Test
	fun `the factory reports no languages`() {
		assertFalse(factory.isAvailable())
		assertFalse(factory.hasLanguage(SpLocale.EN_US))
		assertTrue(factory.availableLocales().isEmpty())
	}

	@Test
	fun `a named locale is refused`() = runTest {
		assertFailsWith<IllegalArgumentException> { factory.createSpellChecker(SpLocale.EN_US) }
	}

	@Test
	fun `the default checker flags nothing and keeps the user dictionary`() = runTest {
		val checker = factory.createSpellChecker()

		assertEquals(emptyList(), checker.checkMultiword("Teh qiuck brwon fox"))
		assertEquals(CorrectWord("brwon"), checker.checkWord("brwon"))
		assertTrue(checker.isWordCorrect("brwon"))

		checker.addToDictionary("Hammer")
		checker.addToDictionary("Kermit", DictionaryScope.System)
		checker.ignoreWord("ignored")
		assertEquals(setOf("hammer", "kermit"), checker.userDictionary())

		checker.removeFromDictionary("HAMMER")
		assertEquals(setOf("kermit"), checker.userDictionary())

		checker.setUserDictionary(listOf("one", " Two "))
		assertEquals(setOf("one", "two"), checker.userDictionary())
		checker.close()
	}

	@Test
	fun `posix locale names convert to SpLocale`() {
		assertEquals(SpLocale("en", "US"), posixLocaleToSpLocale("en_US.UTF-8"))
		assertEquals(SpLocale("de", "DE"), posixLocaleToSpLocale("de_DE@euro"))
		assertEquals(SpLocale("pt", "BR"), posixLocaleToSpLocale("pt_br"))
		assertEquals(SpLocale("fr"), posixLocaleToSpLocale("fr"))
		assertNull(posixLocaleToSpLocale("C.UTF-8"))
		assertNull(posixLocaleToSpLocale("POSIX"))
		assertNull(posixLocaleToSpLocale(""))
		assertNull(posixLocaleToSpLocale(null))
	}
}
