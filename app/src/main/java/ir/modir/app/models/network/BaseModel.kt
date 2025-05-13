package ir.modir.app.models.network

import androidx.compose.runtime.Immutable
import ir.modir.app.di.DI
import ir.modir.app.network.StatusCodes
import kotlinx.coroutines.channels.Channel
import org.koin.core.component.get


@Immutable
sealed class BaseModel<out T> {
    data class Success<T>(val data: T) : BaseModel<T>()
    data class Error(val error: RequestError) : BaseModel<Nothing>()
    data object Loading : BaseModel<Nothing>()

    val stringState: String
        get() = when (this) {
            is Loading -> "loading"
            is Success -> "success"
            is Error -> "error"
        }
}

val <T>BaseModel<T>?.isSuccess: Boolean get() = this is BaseModel.Success
val <T>BaseModel<T>?.isError: Boolean get() = this is BaseModel.Error
val <T>BaseModel<T>?.isLoading: Boolean get() = this is BaseModel.Loading

inline fun <T> BaseModel<T>?.onSuccess(
    block: (BaseModel.Success<T>) -> Unit,
) = apply {
    if (this is BaseModel.Success) {
        block(this)
    }
}

inline fun <T> BaseModel<T>?.onError(
    block: (BaseModel.Error) -> Unit,
) = apply {
    if (this is BaseModel.Error) {
        block((this))
    }
}

fun <T> BaseModel<T>?.asSuccess() = this as BaseModel.Success
fun <T> BaseModel<T>?.asSuccessOrNull() = try {
    asSuccess()
} catch (e: Exception) {
    null
}


sealed class RequestError {
    data class ClientError(
        val message: String,
        val statusCode: Int = -1,
        val exception: Throwable? = null
    ) : RequestError() {
        fun isAuthenticateError(): Boolean {
            return statusCode == StatusCodes.UNAUTHORIZED
        }
    }

    data class ServerError(val statusCode: Int) : RequestError()

    suspend fun sendAsError() {
        DI.get<Channel<RequestError>>().send(this)
    }
}

