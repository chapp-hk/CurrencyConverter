package app.ch.currencyconverter.core

import java.math.BigDecimal

fun BigDecimal?.orDefault() = this ?: BigDecimal(1.0)
