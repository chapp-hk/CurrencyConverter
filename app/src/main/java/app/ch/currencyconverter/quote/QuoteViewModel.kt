package app.ch.currencyconverter.quote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import kotlinx.coroutines.launch

class QuoteViewModel @ViewModelInject
constructor(
    private val getCurrencyCodesUsecase: GetCurrencyCodesUsecase
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
