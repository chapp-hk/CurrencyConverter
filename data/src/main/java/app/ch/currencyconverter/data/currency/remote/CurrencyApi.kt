package app.ch.currencyconverter.data.currency.remote

import retrofit2.http.GET

interface CurrencyApi {

    @GET("list")
    suspend fun getCurrencies(): CurrencyResponse
}
