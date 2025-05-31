package io.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1E88E5)

data class AppColors(
    val text: Color,
    val background: Color,
    val card: Color,
    val navigationBar: Color,
    val textField: TextFieldColors,
    val icon: Color = text,
    val subText: Color,
    val sheet: Color = background,
    val button: Color,
    val textAction: Color,
    val error: Color,
    val divider: Color,
)

data class TextFieldColors(
    val border: Color,
    val cursor: Color,
    val background: Color,
    val placeholder: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> { error("No Colors Provided Yet!") }