package app.ch.currencyconverter.core.recyclerview

import androidx.annotation.LayoutRes

interface ListItem {

    @get:LayoutRes
    val layoutId: Int
}
