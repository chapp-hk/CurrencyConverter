package app.ch.currencyconverter.data.quote.local

interface QuoteLocalStore {

    fun saveLastQuoteTime(time: Long)

    fun isQuoteExpired(): Boolean
}
