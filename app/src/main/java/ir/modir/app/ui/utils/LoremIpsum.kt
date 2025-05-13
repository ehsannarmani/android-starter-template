package ir.modir.app.ui.utils

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun getLorem(words: Int) = LoremIpsum(words).values.joinToString().split("\n").joinToString()