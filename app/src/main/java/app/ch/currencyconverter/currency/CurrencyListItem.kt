package app.ch.currencyconverter.currency

import androidx.lifecycle.MutableLiveData
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.lifecycle.OneOffEvent
import app.ch.currencyconverter.core.recyclerview.ListItem

class CurrencyListItem(
    val code: String,
    val description: String,
    private val currencySelectEvent: MutableLiveData<OneOffEvent<String>>,
    override val layoutId: Int = R.layout.item_currency,
) : ListItem {

    fun selected() {
        currencySelectEvent.value = OneOffEvent(code)
    }
}
