package app.ch.currencyconverter.quote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.data
import app.ch.currencyconverter.domain.base.error
import app.ch.currencyconverter.domain.base.isSuccess
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import app.ch.currencyconverter.domain.quote.usecase.GetCurrencyQuotesUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel @ViewModelInject
constructor(
    private val getCurrencyCodesUsecase: GetCurrencyCodesUsecase,
    private val getCurrencyQuotesUsecase: GetCurrencyQuotesUsecase,
) : ViewModel() {

    private val defaultCode = "USD"

    private val _selectedCode = MutableLiveData(defaultCode)
    val selectedCode: LiveData<String> = _selectedCode

    private val _quoteList = MutableStateFlow<List<QuoteListItem>>(listOf())
    val quoteList: StateFlow<List<QuoteListItem>> = _quoteList

    fun getQuotes() {
        viewModelScope.launch {
            val currencyCodes = getCurrencyCodesUsecase.execute()
            val currencyQuotes = getCurrencyQuotesUsecase.execute()

            if (currencyCodes.isSuccess() && currencyQuotes.isSuccess()) {
                mapCurrencyQuotes(
                    currencyCodes.data().orEmpty(),
                    currencyQuotes.data().orEmpty(),
                )
            } else {
                handleError(
                    currencyCodes.error(),
                    currencyQuotes.error(),
                )
            }
        }
    }

    private fun mapCurrencyQuotes(
        codes: List<CurrencyCode>,
        quotes: Map<String, Double>,
    ) {
        codes.map {
            val srcQuote = quotes["$defaultCode${_selectedCode.value}"] ?: 1.0
            val quote = quotes["$defaultCode${it.code}"]?.div(srcQuote)
            QuoteListItem(it.code, it.description, quote.toString())
        }.let {
            _quoteList.value = it
        }
    }

    private fun handleError(
        codesError: Error?,
        quotesError: Error?,
    ) {
        if (codesError != null) {

        } else if(quotesError != null) {

        }
    }
}
