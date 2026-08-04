package com.darkrockstudios.libs.platformspellchecker

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

// PlatformSpellChecker logs through Napier, which drops everything until a base
// is installed. Without this, the fail-open paths (provider unavailable, request
// timed out, malformed results) are silent and every word looks correctly spelled.
fun initLogging() {
	Napier.base(DebugAntilog())
}
