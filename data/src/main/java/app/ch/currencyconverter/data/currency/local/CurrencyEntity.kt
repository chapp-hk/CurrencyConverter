package app.ch.currencyconverter.data.currency.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(

    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",
)
