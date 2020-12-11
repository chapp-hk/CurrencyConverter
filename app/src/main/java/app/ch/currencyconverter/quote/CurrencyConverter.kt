package app.ch.currencyconverter.quote

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CurrencyConverter @Inject constructor() {

    fun execute(
        srcRate: BigDecimal?,
        dstRate: BigDecimal?,
        amount: BigDecimal?,
    ): String {
        return calculate(srcRate, dstRate, amount)
            .setScale(6, RoundingMode.CEILING).stripTrailingZeros().toString()
    }

    private fun calculate(
        srcRate: BigDecimal?,
        dstRate: BigDecimal?,
        amount: BigDecimal?,
    ): BigDecimal {
        val safeDstRate = dstRate.takeIf { it != BigDecimal.ZERO } ?: BigDecimal.ONE
        return (srcRate ?: BigDecimal.ONE) / safeDstRate * (amount ?: BigDecimal.ONE)
    }
}
