package app.ch.currencyconverter.core.di.feature

import app.ch.currencyconverter.core.localstore.DaoProvider
import app.ch.currencyconverter.data.quote.local.QuoteDao
import app.ch.currencyconverter.data.quote.remote.QuoteApi
import app.ch.currencyconverter.data.quote.repository.QuoteRepositoryImpl
import app.ch.currencyconverter.domain.quote.repository.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class QuoteModule {

    @Provides
    internal fun providesQuoteApi(retrofit: Retrofit): QuoteApi {
        return retrofit.create(QuoteApi::class.java)
    }

    @Provides
    internal fun providesQuoteDao(daoProvider: DaoProvider): QuoteDao {
        return daoProvider.getQuoteDao()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class QuoteRepositoryModule {

    @Binds
    internal abstract fun bindsQuoteRepository(quoteRepositoryImpl: QuoteRepositoryImpl): QuoteRepository
}
