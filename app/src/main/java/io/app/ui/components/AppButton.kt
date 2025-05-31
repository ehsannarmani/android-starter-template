package io.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.app.ui.components.preview.ComponentPreview
import io.app.ui.theme.LocalAppColors
import io.app.ui.utils.getLorem

@Composable
fun AppButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    loading: Boolean = false,
    trailingIcon: Int? = null,
    containerColor: Color = LocalAppColors.current.button,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(45.dp),
        onClick = {
            if (!loading) onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .width(IntrinsicSize.Max), contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                transitionSpec = {
                    scaleIn() togetherWith scaleOut()
                }) {
                if (it) {
                    Box(
                        Modifier
                            .size(27.dp)
                            .align(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .size(27.dp)
                                .align(Alignment.Center),
                            strokeWidth = 3.dp
                        )
                    }
                } else {
                    AnimatedContent(
                        modifier = Modifier.fillMaxWidth(),
                        targetState = text,
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        }
                    ) {
                        AppText(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = it,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            if (trailingIcon != null) {
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AppSmallButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    loading: Boolean = false,
    trailingIcon: Int? = null,
    contentPadding: PaddingValues = PaddingValues(),
    containerColor: Color = LocalAppColors.current.button,
    style: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    ),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(40.dp),
        onClick = {
            if (!loading) onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = contentPadding
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedContent(loading, transitionSpec = {
                scaleIn() togetherWith scaleOut()
            }) {
                if (it) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    AnimatedContent(
                        targetState = text
                    ) {
                        AppText(text = it, style = style, color = style.color)
                    }
                }
            }
            if (trailingIcon != null) {
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    loading: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        color = LocalAppColors.current.textAction,
        fontSize = 15.sp
    ),
    borderColor: Color = LocalAppColors.current.textAction,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Button(
        modifier = modifier
            .height(45.dp),
        onClick = {
            if (!loading) onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = borderColor,
        ),
        shape = shape,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedContent(loading, transitionSpec = {
                scaleIn() togetherWith scaleOut()
            }) {
                if (it) {
                    CircularProgressIndicator(
                        color = LocalAppColors.current.textAction,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .padding(3.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    AppText(
                        text = text,
                        style = textStyle,
                        color = textStyle.color
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    ComponentPreview() {
        val loading = remember { mutableStateOf(false) }
        AppButton(text = "Click On Me!", loading = loading.value) {
            loading.value = !loading.value
        }

        val buttonText = remember { mutableStateOf(getLorem(1)) }
        AppButton(text = buttonText.value) {
            buttonText.value = getLorem(10).split(" ").random()
        }
        AppSmallButton(text = "Click On Me!", loading = loading.value) {
            loading.value = !loading.value

        }
        AppSmallButton(text = "Click On Me!", loading = true) {

        }
        AppOutlinedButton(text = "Click On Me!", loading = loading.value) {
            loading.value = !loading.value

        }
        AppOutlinedButton(text = "Click On Me!", loading = true) {

        }
    }
}