package app.ch.currencyconverter.quote

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.ch.currencyconverter.mockserver.MockWebServerRule
import app.ch.currencyconverter.R
import app.ch.currencyconverter.hasItemAtPosition
import app.ch.currencyconverter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class QuoteFragmentTest {

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
    fun change_amount_will_refresh_quotes() {
        mockWebServerRule.mockSuccess()
        launchNavFragment()

        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("AED")),
                        hasDescendant(withText("3.67297"))
                    )
                )
            )
        )

        onView(withId(R.id.editText)).perform(replaceText("3.3"))
        Thread.sleep(1000)

        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("AED")),
                        hasDescendant(withText("12.1208"))
                    )
                )
            )
        )
    }

    private fun launchNavFragment() {
        launchFragmentInHiltContainer<QuoteFragment> {
            navController.setGraph(R.navigation.main_graph)
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    // The fragment’s view has just been created
                    Navigation.setViewNavController(requireView(), navController)
                }
            }
        }
        Thread.sleep(1000)
    }
}
