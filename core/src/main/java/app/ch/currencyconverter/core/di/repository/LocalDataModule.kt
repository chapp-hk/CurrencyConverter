package app.ch.currencyconverter.core.di.repository

import android.app.Application
import androidx.room.Room
import app.ch.currencyconverter.core.di.localstore.DaoProvider
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
}
