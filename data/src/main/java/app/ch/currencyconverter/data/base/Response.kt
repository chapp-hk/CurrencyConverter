package app.ch.currencyconverter.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class Response(

    @SerialName("success")
    val success: Boolean = false,

    @SerialName("error")
    val error: Error = Error(),
)

@Serializable
data class Error(

    @SerialName("code")
    val code: Int = 0,

    @SerialName("info")
    val info: String = "",
)
