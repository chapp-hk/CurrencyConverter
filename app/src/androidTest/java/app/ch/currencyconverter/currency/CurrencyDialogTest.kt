package app.ch.currencyconverter.currency

import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.di.repository.LocalDataModule
import app.ch.currencyconverter.ktx.launchNavFragment
import app.ch.currencyconverter.mockserver.MockWebServerRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
class CurrencyDialogTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun select_currency() {
        mockWebServerRule.mockSuccess()
        launchNavFragment<CurrencyDialog, CurrencyViewModel>(navController) {
            it.run { navController.setCurrentDestination(R.id.currency) }

            it.parentFragmentManager
                .setFragmentResultListener(
                    "requestKey",
                    it.viewLifecycleOwner
                ) { _, bundle ->
                    expectThat(bundle.getString("code")).isEqualTo("ANG")
                }
        }

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(4, click()))
    }
}
