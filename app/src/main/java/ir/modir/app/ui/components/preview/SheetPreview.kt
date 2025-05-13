package ir.modir.app.ui.components.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.modir.app.ui.theme.AppTheme
import ir.modir.app.ui.theme.LocalAppColors

@Composable
fun SheetPreview(
    paddings: PaddingValues = PaddingValues(
        top = 20.dp
    ),
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                )
                .background(LocalAppColors.current.sheet)
                .padding(paddings)
        ) {
            content()
        }
    }
}

