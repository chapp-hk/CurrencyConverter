package app.ch.currencyconverter.core.localstore

import android.content.SharedPreferences
import androidx.core.content.edit
import app.ch.currencyconverter.core.Constants.PREF_KEY_LAST_QUOTE_TIME
import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppPreference @Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : QuoteLocalStore {

    override fun saveLastQuoteTimeMillis(time: Long) {
        sharedPreferences.edit { putLong(PREF_KEY_LAST_QUOTE_TIME, time) }
    }

    override fun isQuoteExpired(): Boolean {
        return sharedPreferences.getLong(PREF_KEY_LAST_QUOTE_TIME, 0L).let {
            System.currentTimeMillis() - it > TimeUnit.MINUTES.toMillis(30)
        }
    }
}
