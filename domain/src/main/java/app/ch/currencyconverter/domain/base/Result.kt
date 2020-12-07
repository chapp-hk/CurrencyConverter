package app.ch.currencyconverter.domain.base

sealed class Result<out T, out E> {
    class Success<out T>(val data: T) : Result<T, Nothing>()
    class Failure<out E : Error>(val error: E) : Result<Nothing, E>()
}
