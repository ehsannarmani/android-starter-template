package io.meli.vpn.network.error_handler

import ir.modir.app.models.network.RequestError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response

class GlobalErrorHandlerInterceptor(
    private val scope: CoroutineScope,
    private val channel: Channel<RequestError>,
    private val json: Json,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return GlobalErrorHandler(
            request = request,
            response = response,
            scope = scope,
            channel = channel,
            json = json
        ).handle()
    }
}