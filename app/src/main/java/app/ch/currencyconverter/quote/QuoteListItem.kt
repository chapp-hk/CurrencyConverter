package app.ch.currencyconverter.quote

import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.recyclerview.ListItem

data class QuoteListItem(
    val code: String,
    val description: String,
    val quote: String,
    override val layoutId: Int = R.layout.item_quote,
) : ListItem
