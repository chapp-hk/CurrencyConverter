package app.ch.currencyconverter.core.di.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.ch.currencyconverter.core.localstore.AppPreference
import app.ch.currencyconverter.core.localstore.DaoProvider
import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Provides
    internal fun providesDatabase(application: Application): DaoProvider {
        return Room.databaseBuilder(
            application,
            DaoProvider::class.java,
            "database.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    internal fun providesSharedPreference(application: Application): SharedPreferences {
        return application.getSharedPreferences("app_pref", Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppPreferenceModule {

    @Binds
    internal abstract fun bindsLocalStore(appPreference: AppPreference): QuoteLocalStore
}
