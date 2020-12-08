package app.ch.currencyconverter.data.quote.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class QuoteEntity(

    @PrimaryKey
    @ColumnInfo(name = "mapping")
    val mapping: String = "",

    @ColumnInfo(name = "rate")
    val rate: Double = 0.0,
)
