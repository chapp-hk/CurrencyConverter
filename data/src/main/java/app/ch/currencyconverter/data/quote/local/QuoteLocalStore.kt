package app.ch.currencyconverter.data.quote.local

interface QuoteLocalStore {

    fun saveLastQuoteTimeMillis(time: Long)

    fun isQuoteExpired(): Boolean
}
