package io.app.network

import io.app.BuildConfig
import io.app.di.DI
import io.app.viewModels.AppSession
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.get

class RequestConfigurator() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()

        val appSession: AppSession = DI.get()

        val user = appSession.user.value

        if (user?.loggedIn == true) {
            request.addHeader(
                "Authorization",
                "Bearer ${user.authorizationToken}"
            )
        }

        request.addHeader("Accept", "application/json")
        request.addHeader("X-App-Version", BuildConfig.VERSION_CODE.toString())
        return chain.proceed(request.build())
    }
}


