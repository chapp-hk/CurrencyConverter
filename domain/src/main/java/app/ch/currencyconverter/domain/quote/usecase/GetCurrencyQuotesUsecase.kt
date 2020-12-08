package app.ch.currencyconverter.domain.quote.usecase

import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyQuotesUsecase @Inject
constructor(
    private val getCurrencyRepository: CurrencyRepository
) {

    suspend fun execute(): Result<List<CurrencyCode>, Error> {
        return getCurrencyRepository.getCurrencyCodes()
    }
}
