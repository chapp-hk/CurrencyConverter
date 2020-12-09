package app.ch.currencyconverter.quote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.ch.currencyconverter.MainCoroutineRule
import app.ch.currencyconverter.core.Constants.ERROR_NETWORK
import app.ch.currencyconverter.core.lifecycle.OneOffEvent
import app.ch.currencyconverter.domain.base.Error
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import app.ch.currencyconverter.domain.quote.usecase.GetCurrencyQuotesUsecase
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty

class QuoteViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getCurrencyCodesUsecase: GetCurrencyCodesUsecase

    @MockK
    private lateinit var getCurrencyQuotesUsecase: GetCurrencyQuotesUsecase

    private val currencyConverter = CurrencyConverter()
    private lateinit var viewModel: QuoteViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel =
            QuoteViewModel(getCurrencyCodesUsecase, getCurrencyQuotesUsecase, currencyConverter)
    }

    @Test
    fun `get currency code return network error and not get quotes`() {
        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Failure(Error.NetworkError)

        viewModel.getQuotes()

        coVerify {
            getCurrencyCodesUsecase.execute()
        }
        coVerify(exactly = 0) {
            getCurrencyQuotesUsecase.execute()
        }

        viewModel.errorEvent
            .test()
            .assertValue(OneOffEvent(ERROR_NETWORK))

        expectThat(viewModel.quoteList.value).isEmpty()
    }

    @Test
    fun `get quotes return api error`() {
        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Success(
            listOf(CurrencyCode())
        )
        coEvery { getCurrencyQuotesUsecase.execute() } returns Result.Failure(
            Error.ResponseError(404)
        )

        viewModel.getQuotes()

        coVerifySequence {
            getCurrencyCodesUsecase.execute()
            getCurrencyQuotesUsecase.execute()
        }

        viewModel.errorEvent
            .test()
            .assertValue(OneOffEvent(404))

        expectThat(viewModel.quoteList.value).isEmpty()
    }

    @Test
    fun `get quotes and currencies success`() {
        val codes = listOf(
            CurrencyCode("HKD", "Hong Kong Dollar"),
            CurrencyCode("JPY", "Japan Yen"),
        )
        val quotes = mapOf(
            "USDHKD" to 7.75.toBigDecimal(),
            "USDJPY" to 104.26.toBigDecimal(),
        )

        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Success(codes)
        coEvery { getCurrencyQuotesUsecase.execute() } returns Result.Success(quotes)

        viewModel.amount.value = "3"
        viewModel.updateCurrencyCode("HKD")

        coVerifySequence {
            getCurrencyCodesUsecase.execute()
            getCurrencyQuotesUsecase.execute()
        }

        viewModel.errorEvent
            .test()
            .assertNoValue()

        viewModel.selectedCode
            .test()
            .assertValue("HKD")

        viewModel.quoteList.value.forEach { item ->
            expectThat(item).assertThat("assert quote value") {
                it.quote == currencyConverter.execute(
                    quotes["USD${it.code}"],
                    quotes["USDHKD"],
                    3.toBigDecimal()
                )
            }
        }
    }
}
