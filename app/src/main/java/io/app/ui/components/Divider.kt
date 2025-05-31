package io.app.ui.components

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.app.ui.theme.LocalAppColors

@Composable
fun AppHorizontalDivider(
    color: Color = LocalAppColors.current.divider,
    thickness: Dp = DividerDefaults.Thickness
) {
    HorizontalDivider(color = color, thickness = thickness)
}

@Composable
fun AppVerticalDivider() {
    VerticalDivider(color = LocalAppColors.current.divider)
}