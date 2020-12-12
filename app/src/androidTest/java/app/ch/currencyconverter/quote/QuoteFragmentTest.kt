package app.ch.currencyconverter.quote

import androidx.core.os.bundleOf
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isSystemAlertWindow
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.ch.currencyconverter.FileReader
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.Constants.KEY_CODE
import app.ch.currencyconverter.core.Constants.REQUEST_CURRENCY
import app.ch.currencyconverter.core.di.repository.LocalDataModule
import app.ch.currencyconverter.hasItemAtPosition
import app.ch.currencyconverter.ktx.launchNavFragment
import app.ch.currencyconverter.mockserver.MockWebServerRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@RunWith(AndroidJUnit4::class)
@MediumTest
@UninstallModules(LocalDataModule::class)
@HiltAndroidTest
class QuoteFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    private val currencyConverter = CurrencyConverter()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister()
    }

    @Test
    fun change_amount_refresh_quotes() {
        mockWebServerRule.mockSuccess()
        launchNavFragment<QuoteFragment>(navController)

        val initQuote = currencyConverter.execute(
            srcRate = 3.672969.toBigDecimal(),
            dstRate = 1.toBigDecimal(),
            amount = 1.toBigDecimal(),
        )
        Thread.sleep(500)
        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("AED")),
                        hasDescendant(withText(initQuote))
                    )
                )
            )
        )

        onView(withId(R.id.editText)).perform(replaceText("3.3"))

        val updatedQuote = currencyConverter.execute(
            srcRate = 3.672969.toBigDecimal(),
            dstRate = 1.toBigDecimal(),
            amount = 3.3.toBigDecimal(),
        )
        Thread.sleep(500)
        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("AED")),
                        hasDescendant(withText(updatedQuote))
                    )
                )
            )
        )
    }

    @Test
    fun change_amount_and_currency_refresh_quotes() {
        mockWebServerRule.mockSuccess()
        launchNavFragment<QuoteFragment>(navController) {
            it.parentFragmentManager
                .setFragmentResult(REQUEST_CURRENCY, bundleOf(KEY_CODE to "HKD"))
        }

        onView(withId(R.id.tvSelectedCurrency))
            .check(matches(withText("HKD")))

        onView(withId(R.id.editText))
            .perform(replaceText("10"))

        val updatedQuote = currencyConverter.execute(
            srcRate = 3.672969.toBigDecimal(),
            dstRate = 7.75155.toBigDecimal(),
            amount = 10.toBigDecimal(),
        )
        Thread.sleep(500)
        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("AED")),
                        hasDescendant(withText(updatedQuote))
                    )
                )
            )
        )
    }

    @Test
    fun assert_select_currency_navigation() {
        mockWebServerRule.mockSuccess()
        launchNavFragment<QuoteFragment>(navController)

        onView(withId(R.id.btnChange)).perform(click())
        expectThat(navController.currentDestination?.id).isEqualTo(R.id.currency)
    }

    @Test
    fun show_error_toast_when_currency_code_returns_error() {
        mockWebServerRule.mockFailure()
        launchNavFragment<QuoteFragment>(navController = navController)

        Thread.sleep(500)
        onView(withText(R.string.error_api))
            .inRoot(isSystemAlertWindow())
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun show_error_toast_when_currency_quote_returns_error() {
        mockWebServerRule.mockCustomResponse(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setBody(
                    when {
                        request.path.orEmpty().contains("/list") -> FileReader.get("list.json")
                        else -> "custom error"
                    }
                )
            }
        })

        launchNavFragment<QuoteFragment>(navController)

        Thread.sleep(500)
        onView(withText(R.string.error_network))
            .inRoot(isSystemAlertWindow())
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}
