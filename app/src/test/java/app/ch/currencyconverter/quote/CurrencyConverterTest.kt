package app.ch.currencyconverter.quote

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class CurrencyConverterTest {

    private val currencyConverter = CurrencyConverter()

    @Test
    fun `all null values should return 1`() {
        val value = currencyConverter.execute(null, null, null)

        expectThat(value).isEqualTo("1")
    }

    @Test
    fun `dst rate is 0`() {
        val value = currencyConverter.execute(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE)

        expectThat(value).isEqualTo("1")
    }

    @Test
    fun `assert decimal places`() {
        val value = currencyConverter.execute(
            7.75155.toBigDecimal(),
            106.837501.toBigDecimal(),
            20.toBigDecimal()
        )

        expectThat(value).isEqualTo("1.451")
    }
}
