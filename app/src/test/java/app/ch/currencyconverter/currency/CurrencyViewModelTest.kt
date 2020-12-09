package app.ch.currencyconverter.currency

import app.ch.currencyconverter.MainCoroutineRule
import app.ch.currencyconverter.domain.base.Result
import app.ch.currencyconverter.domain.currency.entity.CurrencyCode
import app.ch.currencyconverter.domain.currency.usecase.GetCurrencyCodesUsecase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat

class CurrencyViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var getCurrencyCodesUsecase: GetCurrencyCodesUsecase

    private lateinit var viewModel: CurrencyViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CurrencyViewModel(getCurrencyCodesUsecase)
    }

    @Test
    fun `get currencies`() {
        coEvery { getCurrencyCodesUsecase.execute() } returns Result.Success(
            listOf(
                CurrencyCode("HKD", "Hong Kong Dollar"),
                CurrencyCode("USD", "US Dollar"),
            )
        )

        viewModel.getCurrencies()

        coVerify(exactly = 1) {
            getCurrencyCodesUsecase.execute()
        }

        expectThat(viewModel.currencyList)
            .get { value }
            .assertThat("assert list size") {
                it.size == 2
            }.assertThat("assert values") {
                it[0].run { code == "HKD" && description == "Hong Kong Dollar" } &&
                        it[1].run { code == "USD" && description == "US Dollar" }
            }
    }
}
