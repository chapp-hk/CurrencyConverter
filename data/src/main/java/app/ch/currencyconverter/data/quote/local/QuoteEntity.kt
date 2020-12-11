package app.ch.currencyconverter.data.quote.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import app.ch.currencyconverter.data.base.local.converter.BigDecimalDoubleTypeConverter
import java.math.BigDecimal

@Entity(tableName = "quote")
@TypeConverters(BigDecimalDoubleTypeConverter::class)
data class QuoteEntity(

    @PrimaryKey
    @ColumnInfo(name = "mapping")
    val mapping: String = "",

    @ColumnInfo(name = "rate")
    val rate: BigDecimal = BigDecimal.ZERO,
)
