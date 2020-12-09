package app.ch.currencyconverter.quote

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CurrencyConverter @Inject constructor() {

    private val default = 1.0.toBigDecimal()

    fun execute(
        srcRate: BigDecimal?,
        dstRate: BigDecimal?,
        amount: BigDecimal?,
    ): String {
        return calculate(srcRate, dstRate, amount)
            .setScale(5, RoundingMode.CEILING).stripTrailingZeros().toString()
    }

    private fun calculate(
        srcRate: BigDecimal?,
        dstRate: BigDecimal?,
        amount: BigDecimal?,
    ): BigDecimal {
        return (srcRate ?: default) / (dstRate ?: default) * (amount ?: default)
    }
}
