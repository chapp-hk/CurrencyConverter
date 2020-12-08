package app.ch.currencyconverter.domain.quote.repository

import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result

interface QuoteRepository {

    suspend fun getCurrencyQuotes(): Result<Map<String, Double>, Error>
}
