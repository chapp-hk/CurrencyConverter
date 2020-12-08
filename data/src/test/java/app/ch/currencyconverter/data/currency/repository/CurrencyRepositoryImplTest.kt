package app.ch.currencyconverter.data.currency.repository

import app.ch.currencyconverter.data.base.ErrorResponse
import app.ch.currencyconverter.data.currency.local.CurrencyDao
import app.ch.currencyconverter.data.currency.local.CurrencyEntity
import app.ch.currencyconverter.data.currency.remote.CurrencyApi
import app.ch.currencyconverter.data.currency.remote.CurrencyResponse
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class CurrencyRepositoryImplTest {

    @MockK
    private lateinit var currencyApi: CurrencyApi

    @MockK
    private lateinit var currencyDao: CurrencyDao

    @MockK
    private lateinit var currencyResponse: CurrencyResponse

    private lateinit var currencyRepository: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coEvery { currencyApi.getCurrencies() } returns currencyResponse
        currencyRepository = CurrencyRepositoryImpl(currencyApi, currencyDao)
    }

    @Test
    fun `local is empty and get from remote success`() {
        every { currencyResponse.success } returns true
        every { currencyResponse.currencies } returns mapOf("code" to "description")
        coEvery { currencyDao.getAll() } returns emptyList()
        coEvery { currencyDao.insertAll(any()) } just Runs

        runBlocking {
            val result = currencyRepository.getCurrencyCodes()

            expectThat(result).isA<Result.Success<List<CurrencyCode>>>()
                .get { data }
                .isEqualTo(listOf(CurrencyCode("code", "description")))
        }

        coVerifySequence {
            currencyDao.getAll()
            currencyApi.getCurrencies()
            currencyDao.insertAll(
                mapOf("code" to "description")
                    .map { CurrencyEntity(it.key, it.value) }
            )
        }
    }

    @Test
    fun `local is empty and get from remote return error code`() {
        every { currencyResponse.success } returns false
        every { currencyResponse.errorResponse } returns ErrorResponse(404)
        coEvery { currencyDao.getAll() } returns emptyList()

        runBlocking {
            val result = currencyRepository.getCurrencyCodes()

            expectThat(result).isA<Result.Failure<Error>>()
                .get { error }
                .isA<Error.ResponseError>()
                .get { errorCode }
                .isEqualTo(404)
        }

        coVerifySequence {
            currencyDao.getAll()
            currencyApi.getCurrencies()
        }
        coVerify(exactly = 0) {
            currencyDao.insertAll(any())
        }
    }

    @Test
    fun `local is empty and get from remote throw exception`() {
        coEvery { currencyDao.getAll() } returns emptyList()
        coEvery { currencyApi.getCurrencies() } throws Throwable()

        runBlocking {
            val result = currencyRepository.getCurrencyCodes()

            expectThat(result).isA<Result.Failure<Error>>()
                .get { error }
                .isA<Error.NetworkError>()
        }

        coVerifySequence {
            currencyDao.getAll()
            currencyApi.getCurrencies()
        }
        coVerify(exactly = 0) {
            currencyDao.insertAll(any())
        }
    }

    @Test
    fun `local is not empty and get from local`() {
        coEvery { currencyDao.getAll() } returns listOf(CurrencyEntity("code", "description"))

        runBlocking {
            val result = currencyRepository.getCurrencyCodes()

            expectThat(result).isA<Result.Success<List<CurrencyCode>>>()
                .get { data }
                .isEqualTo(listOf(CurrencyCode("code", "description")))
        }

        coVerify {
            currencyDao.getAll()
        }
        coVerify(exactly = 0) {
            currencyApi.getCurrencies()
            currencyDao.insertAll(any())
        }
    }
}
