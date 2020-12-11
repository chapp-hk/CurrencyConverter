package app.ch.currencyconverter.currency

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ch.currencyconverter.core.lifecycle.LoadingState
import app.ch.currencyconverter.core.lifecycle.OneOffEvent
import app.ch.currencyconverter.domain.base.data
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel @ViewModelInject
constructor(
    private val getCurrencyCodesUsecase: GetCurrencyCodesUsecase,
) : ViewModel(), LoadingState {

    private val _isLoading = MutableLiveData<Boolean>()
    override val isLoading = _isLoading

    private val _currencyList = MutableStateFlow<List<CurrencyListItem>>(listOf())
    val currencyList: StateFlow<List<CurrencyListItem>> = _currencyList

    private val _currencySelectEvent = MutableLiveData<OneOffEvent<String>>()
    val currencySelectEvent: LiveData<OneOffEvent<String>> = _currencySelectEvent

    fun getCurrencies() {
        _isLoading.value = true
        viewModelScope.launch {
            getCurrencyCodesUsecase.execute().data().orEmpty()
                .map { CurrencyListItem(it.code, it.description, _currencySelectEvent) }
                .let {
                    _currencyList.value = it
                    _isLoading.postValue(false)
                }
        }
    }
}
