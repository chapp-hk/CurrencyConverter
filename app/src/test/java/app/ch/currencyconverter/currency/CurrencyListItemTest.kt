package app.ch.currencyconverter.currency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import app.ch.currencyconverter.core.lifecycle.OneOffEvent
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyListItemTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val currencySelectEvent = MutableLiveData(OneOffEvent(""))

    private lateinit var listItem: CurrencyListItem

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        listItem = CurrencyListItem("HKD", "Hong Kong Dollar", currencySelectEvent)
    }

    @Test
    fun `select item should change event`() {
        listItem.selected()

        currencySelectEvent.test()
            .assertValue { it.peek() == "HKD" }
    }
}
