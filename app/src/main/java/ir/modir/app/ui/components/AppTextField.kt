package ir.modir.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.modir.app.ui.components.preview.ComponentPreview
import ir.modir.app.ui.theme.LocalAppColors

@Composable
fun AppTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    focusInitially: Boolean = false,
    label: String? = null,
    value: String,
    error: String? = null,
    placeholder: String? = null,
    ghost: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: @Composable (() -> Unit)? = null
) {
    val colors = LocalAppColors.current.textField
    val positioned = remember {
        mutableStateOf(false)
    }
    val focus = remember {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(positioned.value) {
        if (focusInitially && positioned.value) focusRequester.requestFocus()
    }
    val borderColor by animateColorAsState(
        when {
            ghost -> Color.Transparent
            error != null -> LocalAppColors.current.error
            focus.value -> colors.border
            else -> colors.background
        },
        label = ""
    )
    val labelColor by animateColorAsState(
        when {
            error != null -> LocalAppColors.current.error
            else -> LocalAppColors.current.text
        },
        label = ""
    )
    Column(modifier) {
        if (label != null) {
            AppText(
                text = label,
                color = labelColor
            )
            Spacer(Modifier.height(8.dp))
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val shape = RoundedCornerShape(10.dp)
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(IntrinsicSize.Max)
                    .weight(1f)
                    .clip(shape)
                    .background(if (ghost) Color.Transparent else colors.background)
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = shape
                    )
                    .padding(horizontal = if (ghost) 0.dp else 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    BasicTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                focus.value = it.isFocused
                            }
                            .onGloballyPositioned {
                                positioned.value = true
                            }
                            .fillMaxWidth(),
                        value = value,
                        onValueChange = onValueChange,
                        cursorBrush = SolidColor(if (error != null) LocalAppColors.current.error else colors.cursor),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = LocalAppColors.current.text),
                        keyboardOptions = keyboardOptions,
                        maxLines = maxLines,
                        singleLine = maxLines == 1,
                        visualTransformation = visualTransformation,
                        keyboardActions = keyboardActions
                    )
                }
                Column {
                    AnimatedVisibility(
                        visible = placeholder != null && value.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        AppText(
                            text = placeholder!!,
                            color = if (ghost) LocalAppColors.current.subText else colors.placeholder,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            trailing?.invoke()
        }
        AnimatedVisibility(visible = error != null) {
            Column {
                Spacer(Modifier.height(4.dp))
                AppText(
                    text = error.orEmpty(),
                    color = LocalAppColors.current.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppTextFieldPreview() {
    ComponentPreview() {
        val state = remember { mutableStateOf("") }
        AppTextField(modifier = Modifier.fillMaxWidth(), value = state.value, onValueChange = {
            state.value = it
        }, label = "Email", placeholder = "Your email here")
        AppTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.value,
            onValueChange = {
                state.value = it
            },
            label = "Email",
            placeholder = "Your email here",
            error = "Enter valid email",
            trailing = {
                IconButton(onClick = {
                }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = LocalAppColors.current.icon
                    )
                }
            })
        AppTextField(modifier = Modifier.fillMaxWidth(), value = state.value, onValueChange = {
            state.value = it
        }, placeholder = "Ghost", ghost = true)
    }
}