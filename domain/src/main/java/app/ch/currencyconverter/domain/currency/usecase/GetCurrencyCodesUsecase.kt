package app.ch.currencyconverter.domain.currency.usecase


import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyCodesUsecase @Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend fun execute(): Result<List<CurrencyCode>, Error> {
        return currencyRepository.getCurrencyCodes()
    }
}
