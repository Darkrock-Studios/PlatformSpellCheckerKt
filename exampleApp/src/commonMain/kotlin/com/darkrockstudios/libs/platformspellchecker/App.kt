package com.darkrockstudios.libs.platformspellchecker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darkrockstudios.libs.platformspellchecker.ui.SpellCheckContent
import com.darkrockstudios.libs.platformspellchecker.ui.theme.PlatformSpellCheckerTheme

@Composable
fun App(
	isCompactHeight: Boolean = false
) {
	PlatformSpellCheckerTheme {
		Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
			SpellCheckContent(
				isCompactHeight = isCompactHeight,
				modifier = Modifier.padding(innerPadding)
			)
		}
	}
}
