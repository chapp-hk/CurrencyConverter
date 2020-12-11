package app.ch.currencyconverter.data.base.local.converter

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class BigDecimalDoubleTypeConverterTest {

    private val converter = BigDecimalDoubleTypeConverter()

    @Test
    fun bigDecimalToDouble() {
        expectThat(converter.bigDecimalToDouble(null)).isEqualTo(0.0)
        expectThat(converter.bigDecimalToDouble(BigDecimal.TEN)).isEqualTo(10.0)
    }

    @Test
    fun doubleToBigDecimal() {
        expectThat(converter.doubleToBigDecimal(null)).isEqualTo(BigDecimal.ZERO)
        expectThat(converter.doubleToBigDecimal(3.0)).isEqualTo(3.0.toBigDecimal())
    }
}
