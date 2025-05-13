package ir.modir.app.network

import ir.modir.app.BuildConfig
import ir.modir.app.di.DI
import ir.modir.app.viewModels.AppSession
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.get

class RequestConfigurator() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()

        var newChain = chain
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
        return newChain.proceed(request.build())
    }
}


