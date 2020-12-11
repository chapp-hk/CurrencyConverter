package app.ch.currencyconverter.core.lifecycle

import androidx.lifecycle.LiveData

interface LoadingState {
    val isLoading: LiveData<Boolean>
}
