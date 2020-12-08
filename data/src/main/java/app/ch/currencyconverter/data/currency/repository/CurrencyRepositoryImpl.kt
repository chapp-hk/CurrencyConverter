package app.ch.currencyconverter.data.currency.repository

import app.ch.currencyconverter.data.currency.local.CurrencyDao
import app.ch.currencyconverter.data.currency.local.CurrencyEntity
import app.ch.currencyconverter.data.currency.remote.CurrencyApi
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject
constructor(
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao,
) : CurrencyRepository {

    override suspend fun getCurrencyCodes(): Result<List<CurrencyCode>, Error> {
        return currencyDao.getAll().let {
            if (it.isEmpty()) {
                getFromRemote()
            } else {
                getFromLocal(it)
            }
        }
    }

    private suspend fun getFromRemote(): Result<List<CurrencyCode>, Error> {
        return runCatching {
            currencyApi.getCurrencies().let { response ->
                if (response.success) {
                    response.currencies
                        .map { CurrencyEntity(it.key, it.value) }
                        .also { currencyDao.insertAll(it) }
                        .map { CurrencyCode(it.code, it.description) }
                        .let { Result.Success(it) }
                } else {
                    Result.Failure(Error.ResponseError(response.errorResponse.code))
                }
            }
        }.getOrElse {
            Result.Failure(Error.NetworkError)
        }
    }

    private fun getFromLocal(currencies: List<CurrencyEntity>): Result<List<CurrencyCode>, Error> {
        return currencies.map { CurrencyCode(it.code, it.description) }
            .let { Result.Success(it) }
    }
}
