package app.ch.currencyconverter.core.di.feature

import app.ch.currencyconverter.core.localstore.DaoProvider
import app.ch.currencyconverter.data.currency.local.CurrencyDao
import app.ch.currencyconverter.data.currency.remote.CurrencyApi
import app.ch.currencyconverter.data.currency.repository.CurrencyRepositoryImpl
import app.ch.currencyconverter.domain.currency.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class CurrencyModule {

    @Provides
    internal fun providesCurrencyApi(retrofit: Retrofit): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

    @Provides
    internal fun providesCurrencyDao(daoProvider: DaoProvider): CurrencyDao {
        return daoProvider.getCurrencyDao()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CurrencyRepositoryModule {

    @Binds
    internal abstract fun bindsCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository
}
