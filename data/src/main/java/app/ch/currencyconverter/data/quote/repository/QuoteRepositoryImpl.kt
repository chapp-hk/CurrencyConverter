package app.ch.currencyconverter.data.quote.repository

import app.ch.currencyconverter.data.quote.local.QuoteDao
import app.ch.currencyconverter.data.quote.local.QuoteEntity
import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import app.ch.currencyconverter.data.quote.remote.QuoteApi
import app.ch.currencyconverter.data.quote.remote.QuoteResponse
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.quote.repository.QuoteRepository
import java.math.BigDecimal
import javax.inject.Inject

class QuoteRepositoryImpl @Inject
constructor(
    private val quoteApi: QuoteApi,
    private val quoteDao: QuoteDao,
    private val localStore: QuoteLocalStore,
) : QuoteRepository {

    override suspend fun getCurrencyQuotes(): Result<Map<String, BigDecimal>, Error> {
        return quoteDao.getAll().let {
            if (it.isEmpty() || localStore.isQuoteExpired()) {
                getFromRemote()
            } else {
                getFromLocal(it)
            }
        }
    }

    private suspend fun getFromRemote(): Result<Map<String, BigDecimal>, Error> {
        return runCatching {
            quoteApi.getCurrencies().let { response ->
                if (response.success) {
                    response.also { persistDataToLocal(it) }
                        .quotes
                        .mapValues { BigDecimal(it.value) }
                        .let { Result.Success(it) }
                } else {
                    Result.Failure(Error.ResponseError(response.errorResponse.code))
                }
            }
        }.getOrElse {
            Result.Failure(Error.NetworkError)
        }
    }

    private suspend fun persistDataToLocal(response: QuoteResponse) {
        localStore.saveLastQuoteTimeMillis(System.currentTimeMillis())
        quoteDao.apply {
            deleteAll()
            insertAll(response.quotes.map { QuoteEntity(it.key, it.value) })
        }
    }

    private fun getFromLocal(quotes: List<QuoteEntity>): Result<Map<String, BigDecimal>, Error> {
        return hashMapOf<String, BigDecimal>()
            .apply { quotes.forEach { this[it.mapping] = BigDecimal(it.rate) } }
            .let { Result.Success(it) }
    }
}
