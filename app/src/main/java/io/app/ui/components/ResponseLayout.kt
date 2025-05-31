package io.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.app.R
import io.app.models.network.BaseModel
import io.app.ui.components.preview.ComponentPreview
import io.app.ui.theme.LocalAppColors

@Composable
fun <T> ResponseLayout(
    response: BaseModel<T>,
    doOnRetry: () -> Unit,
    loadingContent: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = LocalAppColors.current.button
            )
        }
    },
    successContent: @Composable (BaseModel.Success<T>) -> Unit,
    errorContent: @Composable (BaseModel.Error) -> Unit = {
        RequestError(onRetry = doOnRetry)
    },
) {
    AnimatedContent(
        targetState = response,
        transitionSpec = {
            fadeIn(tween(500)) togetherWith fadeOut(tween(500))
        },
        contentKey = {
            it.stringState
        },
    ) {
        when (it) {
            is BaseModel.Error -> errorContent(it)
            BaseModel.Loading -> loadingContent()
            is BaseModel.Success -> successContent(it)
        }
    }
}

@Composable
fun RequestError(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppSmallButton(
            modifier = Modifier.width(100.dp),
            onClick = onRetry,
            text = stringResource(R.string.retry)
        )
    }
}

@Preview
@Composable
private fun RequestErrorPreview() {
    ComponentPreview {
        RequestError {

        }
    }
}