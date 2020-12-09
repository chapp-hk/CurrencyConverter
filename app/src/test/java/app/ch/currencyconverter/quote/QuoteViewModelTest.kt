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
import strikt.assertions.isNotEmpty

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

    private lateinit var viewModel: QuoteViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = QuoteViewModel(getCurrencyCodesUsecase, getCurrencyQuotesUsecase)
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
        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Success(
            listOf(
                CurrencyCode("HKD", "Hong Kong Dollar"),
                CurrencyCode("JPY", "Japan Yen"),
            )
        )
        coEvery { getCurrencyQuotesUsecase.execute() } returns Result.Success(
            mapOf(
                "USDHKD" to 7.75.toBigDecimal(),
                "USDJPY" to 104.26.toBigDecimal(),
            )
        )

        viewModel.getQuotes()

        coVerifySequence {
            getCurrencyCodesUsecase.execute()
            getCurrencyQuotesUsecase.execute()
        }

        viewModel.errorEvent
            .test()
            .assertNoValue()

        expectThat(viewModel.quoteList.value)
            .isNotEmpty()
    }

    @Test
    fun `update currency code should refresh currency quotes`() {
        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Success(
            listOf(
                CurrencyCode("HKD", "Hong Kong Dollar"),
                CurrencyCode("JPY", "Japan Yen"),
            )
        )
        coEvery { getCurrencyQuotesUsecase.execute() } returns Result.Success(
            mapOf(
                "USDHKD" to 7.75.toBigDecimal(),
                "USDJPY" to 104.26.toBigDecimal(),
            )
        )

        viewModel.updateCurrencyCode("HKD")

        coVerifySequence {
            getCurrencyCodesUsecase.execute()
            getCurrencyQuotesUsecase.execute()
        }

        viewModel.selectedCode
            .test()
            .assertValue("HKD")
    }
}
