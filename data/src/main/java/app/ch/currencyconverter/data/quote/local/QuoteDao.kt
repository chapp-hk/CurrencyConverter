package app.ch.currencyconverter.data.quote.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<QuoteEntity>)

    @Query("SELECT * from quote")
    suspend fun getAll(): List<QuoteEntity>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()
}
