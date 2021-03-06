package app.ch.currencyconverter.domain.quote.usecase

import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.quote.repository.QuoteRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetCurrencyQuotesUsecase @Inject
constructor(
    private val quoteRepository: QuoteRepository
) {

    suspend fun execute(): Result<Map<String, BigDecimal>, Error> {
        return quoteRepository.getCurrencyQuotes()
    }
}
