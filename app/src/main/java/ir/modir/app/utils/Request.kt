package ir.modir.app.utils

import ir.modir.app.BuildConfig
import ir.modir.app.di.DI
import ir.modir.app.models.network.BaseModel
import ir.modir.app.models.network.Error
import ir.modir.app.models.network.RequestError
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import org.koin.core.component.get
import retrofit2.Response
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

suspend fun <T> request(
    reportErrors: Boolean = true,
    timeout: Duration = Constants.REQUEST_TIMEOUT.seconds,
    request: suspend () -> Response<T>,
): BaseModel<T> {
    val json = DI.get<Json>()
    var response: BaseModel<T>
    val startTime = System.currentTimeMillis()
    try {
        withTimeout(timeout) {
            request().also {
                response = if (it.isSuccessful) {
                    BaseModel.Success(data = it.body()!!)
                } else {
                    when (val statusCode = it.code()) {
                        in 400..499 -> {
                            BaseModel.Error(
                                RequestError.ClientError(
                                    message = json.decodeFromString<Error>(
                                        it.errorBody()?.string().toString(),
                                    ).message,
                                    statusCode = it.code()
                                )
                            )
                        }

                        in 500..599 -> {
                            BaseModel.Error(RequestError.ServerError(statusCode = statusCode))
                        }

                        else -> error("Unknown error received: $statusCode")
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        val error = RequestError.ClientError(message = e.message.toString(), exception = e)
        response = BaseModel.Error(
            error = error,
        )
        if (reportErrors) error.sendAsError()
    }
    val endTime = System.currentTimeMillis()
    if (BuildConfig.DEBUG) {
        println("Request Response: $response")
        println("Measured Response Time: ${endTime - startTime}")
    }
    return response
}
