package app.ch.currencyconverter.data.quote.repository

import app.ch.currencyconverter.data.base.ErrorResponse
import app.ch.currencyconverter.data.quote.local.QuoteDao
import app.ch.currencyconverter.data.quote.local.QuoteEntity
import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import app.ch.currencyconverter.data.quote.remote.QuoteApi
import app.ch.currencyconverter.data.quote.remote.QuoteResponse
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class QuoteRepositoryImplTest {

    @MockK
    private lateinit var quoteApi: QuoteApi

    @MockK
    private lateinit var quoteDao: QuoteDao

    @MockK
    private lateinit var localStore: QuoteLocalStore

    @MockK
    private lateinit var quoteResponse: QuoteResponse

    private lateinit var quoteRepository: QuoteRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coEvery { quoteApi.getCurrencies() } returns quoteResponse
        coEvery { quoteDao.deleteAll() } just Runs
        coEvery { quoteDao.insertAll(any()) } just Runs
        coEvery { localStore.saveLastQuoteTimeMillis(any()) } just Runs
        quoteRepository = QuoteRepositoryImpl(quoteApi, quoteDao, localStore)
    }

    @Test
    fun `local is empty and get from remote success`() {
        every { quoteResponse.success } returns true
        every { quoteResponse.quotes } returns mapOf("mapping" to 1.0)
        coEvery { quoteDao.getAll() } returns emptyList()

        runBlocking {
            val result = quoteRepository.getCurrencyQuotes()

            expectThat(result).isA<Result.Success<Map<String, BigDecimal>>>()
                .get { data }
                .isEqualTo(mapOf("mapping" to BigDecimal(1.0)))
        }

        coVerifySequence {
            quoteDao.getAll()
            quoteApi.getCurrencies()
            localStore.saveLastQuoteTimeMillis(any())
            quoteDao.deleteAll()
            quoteDao.insertAll(
                mapOf("mapping" to 1.0)
                    .map { QuoteEntity(it.key, it.value) }
            )
        }
        verify(exactly = 0) {
            localStore.isQuoteExpired()
        }
    }

    @Test
    fun `local is expired and get from remote success`() {
        every { quoteResponse.success } returns true
        every { quoteResponse.quotes } returns mapOf("mapping" to 1.0)
        every { localStore.isQuoteExpired() } returns true
        coEvery { quoteDao.getAll() } returns listOf(QuoteEntity())

        runBlocking {
            val result = quoteRepository.getCurrencyQuotes()

            expectThat(result).isA<Result.Success<Map<String, BigDecimal>>>()
                .get { data }
                .isEqualTo(mapOf("mapping" to BigDecimal(1.0)))
        }

        coVerifySequence {
            quoteDao.getAll()
            localStore.isQuoteExpired()
            quoteApi.getCurrencies()
            localStore.saveLastQuoteTimeMillis(any())
            quoteDao.deleteAll()
            quoteDao.insertAll(
                mapOf("mapping" to 1.0)
                    .map { QuoteEntity(it.key, it.value) }
            )
        }
    }

    @Test
    fun `get from remote return error code`() {
        every { quoteResponse.success } returns false
        every { quoteResponse.errorResponse } returns ErrorResponse(500)
        coEvery { quoteDao.getAll() } returns emptyList()

        runBlocking {
            val result = quoteRepository.getCurrencyQuotes()

            expectThat(result).isA<Result.Failure<Error>>()
                .get { error }
                .isA<Error.ResponseError>()
                .get { errorCode }
                .isEqualTo(500)
        }

        coVerify(exactly = 0) {
            localStore.saveLastQuoteTimeMillis(any())
            quoteDao.deleteAll()
            quoteDao.insertAll(any())
        }
    }

    @Test
    fun `get from remote throw exception`() {
        coEvery { quoteDao.getAll() } returns emptyList()
        coEvery { quoteApi.getCurrencies() } throws Throwable()

        runBlocking {
            val result = quoteRepository.getCurrencyQuotes()

            expectThat(result).isA<Result.Failure<Error>>()
                .get { error }
                .isA<Error.NetworkError>()
        }

        coVerify(exactly = 0) {
            localStore.saveLastQuoteTimeMillis(any())
            quoteDao.deleteAll()
            quoteDao.insertAll(any())
        }
    }

    @Test
    fun `get from local`() {
        every { localStore.isQuoteExpired() } returns false
        coEvery { quoteDao.getAll() } returns listOf(QuoteEntity("mapping", 3.0))

        runBlocking {
            val result = quoteRepository.getCurrencyQuotes()

            expectThat(result).isA<Result.Success<Map<String, BigDecimal>>>()
                .get { data }
                .isEqualTo(mapOf("mapping" to BigDecimal(3.0)))
        }

        coVerify(exactly = 0) {
            quoteApi.getCurrencies()
            localStore.saveLastQuoteTimeMillis(any())
            quoteDao.deleteAll()
            quoteDao.insertAll(any())
        }
    }
}
