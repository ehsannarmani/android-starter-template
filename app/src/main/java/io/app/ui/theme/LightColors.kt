package io.app.ui.theme

import androidx.compose.ui.graphics.Color

val LightColors = AppColors(
    text = Color(0xff313131),
    background = Color(0xffcecece),
    card = Color(0xFFBDBDBD),
    navigationBar = Color(0xFFBDBDBD),
    textField = TextFieldColors(
        border = Primary,
        cursor = Color(0xff313131),
        background = Color(0xFFBDBDBD),
        placeholder = Color.Gray
    ),
    subText = Color(0xff717171),
    button = Primary,
    textAction = Primary,
    error = Color(0xFFE53935),
    divider = Color(0xff919191)
)