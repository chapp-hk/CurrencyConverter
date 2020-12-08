package app.ch.currencyconverter.data.currency.remote

import app.ch.currencyconverter.data.base.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(

    @SerialName("currencies")
    val currencies: Map<String, String> = mapOf(),
) : Response()
