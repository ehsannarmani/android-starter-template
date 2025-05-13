package ir.modir.app

import android.app.Application
import androidx.datastore.core.DataStore
import io.meli.vpn.network.error_handler.GlobalErrorHandlerInterceptor
import io.meli.vpn.serializers.datastore.createDatastore
import ir.modir.app.di.KoinTags
import ir.modir.app.models.network.RequestError
import ir.modir.app.network.EndPoints
import ir.modir.app.network.RequestConfigurator
import ir.modir.app.network.api.AppApi
import ir.modir.app.network.api.AuthApi
import ir.modir.app.utils.Constants
import ir.modir.app.utils.onBuildTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                encodeDefaults = true
                coerceInputValues = true
            }

            modules(module {
                single {
                    CoroutineScope(SupervisorJob() + Dispatchers.Default)
                }
                single {
                    Channel<RequestError>()
                }
                single {
                    val client = OkHttpClient.Builder()
                        .readTimeout(Constants.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .connectTimeout(Constants.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .writeTimeout(Constants.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .callTimeout(Constants.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .addInterceptor(RequestConfigurator())
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = onBuildTypes(
                                release = HttpLoggingInterceptor.Level.NONE,
                                debug = HttpLoggingInterceptor.Level.BODY
                            )
                        })
                        .addInterceptor(GlobalErrorHandlerInterceptor(get(), get(), get()))
                        .build()
                    Retrofit.Builder()
                        .baseUrl(EndPoints.BASE_URL)
                        .client(client)
                        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                        .build()
                }
                single { json }
                single { get<Retrofit>().create(AuthApi::class.java) }
                single { get<Retrofit>().create(AppApi::class.java) }
                singleDatastore(
                    tag = KoinTags.USER_DATASTORE,
                    preferencesName = Constants.Prefs.USER_PREFERENCES,
                    default = {}
                )
            })
        }
    }

    private inline fun <reified T> createDatastore(
        json: Json,
        preferencesName: String,
        crossinline default: () -> T,
    ): DataStore<T> {
        return createDatastore(
            json = json,
            context = this,
            preferencesName = preferencesName,
            default = { default() }
        )
    }

    private inline fun <reified T> Module.singleDatastore(
        tag: StringQualifier,
        preferencesName: String,
        crossinline default: () -> T,
    ) = single(tag) {
        createDatastore(
            json = get(),
            preferencesName = preferencesName,
            default = default
        )
    }
}