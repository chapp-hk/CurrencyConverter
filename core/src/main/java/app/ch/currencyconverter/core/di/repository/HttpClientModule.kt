package app.ch.currencyconverter.core.di.repository

import app.ch.currencyconverter.core.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

@Module(
    includes = [
        HttpConfigModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class HttpClientModule {

    @ExperimentalSerializationApi
    @Provides
    internal fun providesConverterFactory(): Converter.Factory {
        return Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    internal fun providesRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.request()
                .url
                .newBuilder()
                .addQueryParameter("access_key", BuildConfig.API_KEY)
                .build()
                .let { chain.proceed(chain.request().newBuilder().url(it).build()) }
        }
    }

    @Provides
    internal fun providesOKHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    internal fun providesRetrofit(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
        baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }
}
