package app.ch.currencyconverter.core.localstore

import android.content.SharedPreferences
import androidx.core.content.edit
import app.ch.currencyconverter.data.quote.local.QuoteLocalStore
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppPreference @Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : QuoteLocalStore {

    override fun saveLastQuoteTime(time: Long) {
        sharedPreferences.edit { putLong("pref_last_quote_time", time) }
    }

    override fun isQuoteExpired(): Boolean {
        return sharedPreferences.getLong("pref_last_quote_time", 0L).let {
            TimeUnit.SECONDS.toMillis(it) - System.currentTimeMillis() >
                    TimeUnit.MINUTES.toMillis(30)
        }
    }
}
