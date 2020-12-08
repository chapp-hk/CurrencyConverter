package app.ch.currencyconverter.core.di.localstore

import androidx.room.Database
import androidx.room.RoomDatabase
import app.ch.currencyconverter.data.currency.local.CurrencyDao
import app.ch.currencyconverter.data.currency.local.CurrencyEntity

@Database(
    entities = [
        CurrencyEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DaoProvider : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao
}
