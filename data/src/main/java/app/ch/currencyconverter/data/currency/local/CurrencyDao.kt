package app.ch.currencyconverter.data.currency.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("SELECT * from currency")
    suspend fun getAll(): List<CurrencyEntity>
}
