package app.ch.currencyconverter.quote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ch.currencyconverter.core.Constants.ERROR_NETWORK
import app.ch.currencyconverter.core.lifecycle.OneOffEvent
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import app.ch.currencyconverter.domain.quote.usecase.GetCurrencyQuotesUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class QuoteViewModel @ViewModelInject
constructor(
    private val getCurrencyCodesUsecase: GetCurrencyCodesUsecase,
    private val getCurrencyQuotesUsecase: GetCurrencyQuotesUsecase,
    private val currencyConverter: CurrencyConverter,
) : ViewModel() {

    private val defaultCode = "USD"

    val amount = MutableLiveData<String>()

    private val _selectedCode = MutableLiveData(defaultCode)
    val selectedCode: LiveData<String> = _selectedCode

    private val _errorEvent = MutableLiveData<OneOffEvent<Int>>()
    val errorEvent: LiveData<OneOffEvent<Int>> = _errorEvent

    private val _quoteList = MutableStateFlow<List<QuoteListItem>>(listOf())
    val quoteList: StateFlow<List<QuoteListItem>> = _quoteList

    fun updateCurrencyCode(code: String) {
        _selectedCode.value = code
        getQuotes()
    }

    fun getQuotes() {
        viewModelScope.launch {
            getCurrencyCodesUsecase.execute().let {
                when (it) {
                    is Result.Success -> getCurrencyQuotes(it.data)
                    is Result.Failure -> handleError(it.error)
                }
            }
        }
    }

    private suspend fun getCurrencyQuotes(codes: List<CurrencyCode>) {
        getCurrencyQuotesUsecase.execute().let {
            when (it) {
                is Result.Success -> updateCurrencyQuotes(codes, it.data)
                is Result.Failure -> handleError(it.error)
            }
        }
    }

    private fun updateCurrencyQuotes(
        codes: List<CurrencyCode>,
        quotes: Map<String, BigDecimal>,
    ) {
        val targetQuote = quotes[getQuoteKey(_selectedCode.value)]

        codes.map { currencyCode ->
            currencyConverter.execute(
                srcRate = quotes[getQuoteKey(currencyCode.code)],
                dstRate = targetQuote,
                amount = amount.value?.toBigDecimalOrNull(),
            ).let {
                QuoteListItem(
                    currencyCode.code,
                    currencyCode.description,
                    it,
                )
            }
        }.let {
            _quoteList.value = it
        }
    }

    private fun handleError(error: Error) {
        when (error) {
            is Error.NetworkError -> ERROR_NETWORK
            is Error.ResponseError -> error.errorCode
        }.let {
            _errorEvent.value = OneOffEvent(it)
        }
    }

    private fun getQuoteKey(code: String?): String {
        return "$defaultCode$code"
    }
}
