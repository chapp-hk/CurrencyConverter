package app.ch.currencyconverter.data.quote.repository

import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import app.ch.currencyconverter.data.quote.local.QuoteDao
import app.ch.currencyconverter.data.quote.local.QuoteEntity
import app.ch.currencyconverter.data.quote.remote.QuoteApi
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.quote.repository.QuoteRepository
import javax.inject.Inject

class QuoteRepositoryImpl @Inject
constructor(
    private val quoteApi: QuoteApi,
    private val quoteDao: QuoteDao,
    private val localStore: QuoteLocalStore,
) : QuoteRepository {

    override suspend fun getCurrencyQuotes(): Result<Map<String, Double>, Error> {
        return quoteDao.getAll().let {
            if (it.isEmpty() || localStore.isQuoteExpired()) {
                getFromRemote()
            } else {
                getFromLocal(it)
            }
        }
    }

    private suspend fun getFromRemote(): Result<Map<String, Double>, Error> {
        return runCatching {
            quoteApi.getCurrencies().let { response ->
                if (response.success) {
                    response.apply { localStore.saveLastQuoteTime(timestamp) }
                        .let { it.quotes.apply { refreshLocalDb(this) } }
                        .let { Result.Success(it) }
                } else {
                    Result.Failure(Error.ResponseError(response.errorResponse.code))
                }
            }
        }.getOrElse {
            Result.Failure(Error.NetworkError)
        }
    }

    private suspend fun refreshLocalDb(map: Map<String, Double>) {
        map.map { QuoteEntity(it.key, it.value) }
            .also {
                quoteDao.deleteAll()
                quoteDao.insertAll(it)
            }
    }

    private fun getFromLocal(quotes: List<QuoteEntity>): Result<Map<String, Double>, Error> {
        return hashMapOf<String, Double>().apply {
            quotes.forEach { this[it.mapping] = it.rate }
        }.let { Result.Success(it) }
    }
}
