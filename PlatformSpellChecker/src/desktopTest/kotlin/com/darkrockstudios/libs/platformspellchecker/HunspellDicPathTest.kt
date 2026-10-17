package com.darkrockstudios.libs.platformspellchecker

import com.darkrockstudios.libs.platformspellchecker.linux.HunspellLibrary
import com.darkrockstudios.libs.platformspellchecker.linux.HunspellWrapper
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HunspellDicPathTest {

	@get:Rule
	val tmp = TemporaryFolder()

	private fun dictionaryDir(vararg names: String): File {
		val dir = tmp.newFolder()
		names.forEach { name ->
			File(dir, "$name.aff").writeText("SET UTF-8\n")
			File(dir, "$name.dic").writeText("0\n")
		}
		return dir
	}

	@Test
	fun `a dictionary only in DICPATH is found`() {
		val dir = dictionaryDir("xx_YY")

		val files = HunspellWrapper.findDictionaryFiles("xx-YY", dicPath = dir.path)

		assertEquals(File(dir, "xx_YY.aff").path to File(dir, "xx_YY.dic").path, files)
	}

	@Test
	fun `a dictionary in no searched directory is not found`() {
		assertNull(HunspellWrapper.findDictionaryFiles("xx-YY", dicPath = null))
	}

	@Test
	fun `every DICPATH entry is searched`() {
		val empty = tmp.newFolder()
		val dir = dictionaryDir("xx_YY")
		val dicPath = listOf(empty.path, "", dir.path).joinToString(File.pathSeparator)

		val files = HunspellWrapper.findDictionaryFiles("xx-YY", dicPath = dicPath)

		assertEquals(File(dir, "xx_YY.dic").path, files?.second)
	}

	@Test
	fun `DICPATH takes precedence over the system directories`() {
		val dir = dictionaryDir("en_US")

		val files = HunspellWrapper.findDictionaryFiles("en-US", dicPath = dir.path)

		assertEquals(File(dir, "en_US.dic").path, files?.second)
	}

	@Test
	fun `languages in DICPATH are listed as available`() {
		val dir = dictionaryDir("xx_YY")

		assertContains(HunspellWrapper.getAvailableLanguages(dicPath = dir.path), "xx-YY")
		assertFalse("xx-YY" in HunspellWrapper.getAvailableLanguages(dicPath = null))
	}

	@Test
	fun `a checker spells with a dictionary found through DICPATH`() {
		assumeTrue("Not running on Linux", System.getProperty("os.name").lowercase().contains("linux"))
		assumeTrue("Hunspell library not available", HunspellLibrary.isAvailable())
		val (aff, dic) = HunspellWrapper.findDictionaryFiles("en-US", dicPath = null)
			?: return assumeTrue("en-US dictionary not available", false)
		val dir = tmp.newFolder()
		File(aff).copyTo(File(dir, "xx_YY.aff"))
		File(dic).copyTo(File(dir, "xx_YY.dic"))

		val checker = assertNotNull(HunspellWrapper.create("xx-YY", dicPath = dir.path))

		checker.use {
			assertTrue(it.isWordCorrect("sentence"))
			assertFalse(it.isWordCorrect("sentense"))
		}
	}
}
