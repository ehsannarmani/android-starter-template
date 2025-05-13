package ir.modir.app.ui.components.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.modir.app.ui.theme.AppTheme
import ir.modir.app.ui.theme.LocalAppColors
import ir.modir.app.ui.theme.ScreenPaddings

@Composable
fun ScreenPreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    applyPaddings: Boolean = true,
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme = darkTheme) {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAppColors.current.background)
                .padding(if (applyPaddings) ScreenPaddings else 0.dp)
        ) {
            content()
        }
    }
}