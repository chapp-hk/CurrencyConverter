package app.ch.currencyconverter.domain.base

sealed class Error {
    object NetworkError : Error()
    class ResponseError(val errorCode: Int) : Error()
}
