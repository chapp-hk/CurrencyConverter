package app.ch.currencyconverter.data.quote.remote

import app.ch.currencyconverter.data.base.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(

    @SerialName("timestamp")
    val timestamp: Long = 0,

    @SerialName("quotes")
    val quotes: Map<String, Double> = mapOf(),
) : Response()
