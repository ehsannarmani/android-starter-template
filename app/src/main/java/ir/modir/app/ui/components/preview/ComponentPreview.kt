package ir.modir.app.ui.components.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.modir.app.ui.theme.AppTheme
import ir.modir.app.ui.theme.LocalAppColors

@Composable
fun ComponentPreview(
    paddingValues: PaddingValues = PaddingValues(32.dp),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current.background)
                .clip(RoundedCornerShape(8.dp))
                .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            content()
        }
    }
}