package app.ch.currencyconverter.core.localstore

import android.content.SharedPreferences
import app.ch.currencyconverter.core.Constants.PREF_KEY_LAST_QUOTE_TIME
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class AppPreferenceTest {

    @MockK
    private lateinit var sharedPreferences: SharedPreferences

    @MockK
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var appPreference: AppPreference

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { sharedPreferences.edit() } returns editor
        every { editor.putLong(any(), any()) } returns editor
        every { editor.apply() } just runs
        appPreference = AppPreference(sharedPreferences)
    }

    @Test
    fun saveLastQuoteTimeMillis() {
        appPreference.saveLastQuoteTimeMillis(56789)

        verifySequence {
            editor.putLong(PREF_KEY_LAST_QUOTE_TIME, 56789)
            editor.apply()
        }
    }

    @Test
    fun `isQuoteExpired return true`() {
        every {
            sharedPreferences.getLong(any(), any())
        } returns System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(40)

        assertTrue(appPreference.isQuoteExpired())
    }

    @Test
    fun `isQuoteExpired return false`() {
        every {
            sharedPreferences.getLong(any(), any())
        } returns System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10)

        assertFalse(appPreference.isQuoteExpired())
    }
}
