package app.ch.currencyconverter.domain.currency.repository

import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode

interface CurrencyRepository {

    suspend fun getCurrencyCodes(): Result<List<CurrencyCode>, Error>
}
