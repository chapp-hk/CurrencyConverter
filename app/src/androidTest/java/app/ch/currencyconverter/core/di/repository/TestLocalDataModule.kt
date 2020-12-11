package app.ch.currencyconverter.core.di.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.ch.currencyconverter.core.localstore.DaoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class TestLocalDataModule {

    @Provides
    internal fun providesDatabase(application: Application): DaoProvider {
        return Room.inMemoryDatabaseBuilder(
            application,
            DaoProvider::class.java
        ).build()
    }

    @Provides
    internal fun providesSharedPreference(application: Application): SharedPreferences {
        return application.getSharedPreferences("app_pref_test", Context.MODE_PRIVATE)
    }
}
