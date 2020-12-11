package app.ch.currencyconverter.data.base.local.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalDoubleTypeConverter {

    @TypeConverter
    fun bigDecimalToDouble(input: BigDecimal?): Double {
        return input?.toDouble() ?: 0.0
    }

    @TypeConverter
    fun doubleToBigDecimal(input: Double?): BigDecimal {
        return input?.toBigDecimal() ?: BigDecimal.ZERO
    }
}
