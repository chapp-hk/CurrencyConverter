package app.ch.currencyconverter.core.di

import app.ch.currencyconverter.core.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class HttpConfigModule {

    @Provides
    fun providesHttpLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}
