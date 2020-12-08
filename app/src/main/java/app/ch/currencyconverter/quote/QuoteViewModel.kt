package app.ch.currencyconverter.quote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import app.ch.currencyconverter.domain.quote.usecase.GetCurrencyQuotesUsecase
import kotlinx.coroutines.launch

class QuoteViewModel @ViewModelInject
constructor(
    private val getCurrencyCodesUsecase: GetCurrencyCodesUsecase,
    private val getCurrencyQuotesUsecase: GetCurrencyQuotesUsecase,
) : ViewModel() {

    fun getQuotes() {
        viewModelScope.launch {
            when (getCurrencyCodesUsecase.execute()) {
                is Result.Success -> {

                }
                is Result.Failure -> {

                }
            }
        }
    }
}
