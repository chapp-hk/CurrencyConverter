package app.ch.currencyconverter.data.quote.remote

import app.ch.currencyconverter.data.base.Response
import app.ch.currencyconverter.data.base.remote.serializer.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class QuoteResponse(

    @SerialName("quotes")
    val quotes: Map<String, @Serializable(with = BigDecimalSerializer::class) BigDecimal> = mapOf(),
) : Response()
