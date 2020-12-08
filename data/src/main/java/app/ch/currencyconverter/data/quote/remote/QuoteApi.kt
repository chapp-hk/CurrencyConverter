package app.ch.currencyconverter.data.quote.remote

import retrofit2.http.GET

interface QuoteApi {

    @GET("live")
    suspend fun getCurrencies(): QuoteResponse
}
