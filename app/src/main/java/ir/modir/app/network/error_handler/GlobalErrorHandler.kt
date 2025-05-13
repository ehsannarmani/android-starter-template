package io.meli.vpn.network.error_handler

import ir.modir.app.models.network.Error
import ir.modir.app.models.network.RequestError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import net.sitano.common.network.error_handler.AllErrorsStrategy
import net.sitano.common.network.error_handler.ClientErrorsStrategy
import net.sitano.common.network.error_handler.IgnoreGlobalErrorHandling
import net.sitano.common.network.error_handler.ServerErrorsStrategy
import net.sitano.common.network.error_handler.StatusCodeStrategy
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation

class GlobalErrorHandler(
    private val request: Request,
    private val response: Response,
    private val scope: CoroutineScope,
    private val channel: Channel<RequestError>,
    private val json: Json,
) {

    private var originalResponse: Response
    private var stringBody: String
    var body: ResponseBody? = null

    init {
        body = response.body
        stringBody = body?.string() ?: ""
        originalResponse = response.newBuilder()
            .body(stringBody.toResponseBody(body?.contentType()))
            .build()
    }

    fun handle(): Response {
        val method = request.tag(Invocation::class.java)?.method()
        val annotation = method?.getAnnotation(IgnoreGlobalErrorHandling::class.java)
        val ignoreStrategy = annotation?.ignoreStrategy
        if (ignoreStrategy == AllErrorsStrategy::class) return originalResponse
        if (!response.isSuccessful) {
            val statusCode = response.code
            if (ignoreStrategy == StatusCodeStrategy::class && statusCode in annotation.ignoreStatusCodes) return originalResponse
            when (statusCode) {
                in 400..499 -> {
                    // client error
                    if (ignoreStrategy == ClientErrorsStrategy::class) return originalResponse
                    runCatching {
                        json.decodeFromString<Error>(stringBody)
                    }
                        .onSuccess {
                            scope.launch {
                                channel.send(
                                    RequestError.ClientError(
                                        message = it.errors ?: it.message,
                                        statusCode = statusCode,
                                    )
                                )
                            }
                        }
                        .onFailure {
                            scope.launch {
                                channel.send(
                                    RequestError.ClientError(
                                        message = stringBody,
                                        statusCode = statusCode,
                                        exception = it
                                    )
                                )
                            }
                        }
                }

                in 500..599 -> {
                    // server error
                    if (annotation?.ignoreStrategy == ServerErrorsStrategy::class) return originalResponse
                    scope.launch {
                        channel.send(RequestError.ServerError(statusCode = statusCode))
                    }
                }
            }

        }
        return originalResponse
    }
}