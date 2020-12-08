package app.ch.currencyconverter.core.localstore

import androidx.room.Database
import androidx.room.RoomDatabase
import app.ch.currencyconverter.data.currency.local.CurrencyDao
import app.ch.currencyconverter.data.currency.local.CurrencyEntity
import app.ch.currencyconverter.data.quote.local.QuoteDao
import app.ch.currencyconverter.data.quote.local.QuoteEntity

@Database(
    entities = [
        CurrencyEntity::class,
        QuoteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DaoProvider : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

    abstract fun getQuoteDao(): QuoteDao
}
