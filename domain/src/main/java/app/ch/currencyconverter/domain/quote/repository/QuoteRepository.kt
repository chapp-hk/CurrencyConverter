package app.ch.currencyconverter.domain.quote.repository

import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import java.math.BigDecimal

interface QuoteRepository {

    suspend fun getCurrencyQuotes(): Result<Map<String, BigDecimal>, Error>
}
