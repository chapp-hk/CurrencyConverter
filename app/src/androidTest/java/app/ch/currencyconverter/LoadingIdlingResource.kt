package app.ch.currencyconverter

import androidx.test.espresso.IdlingResource
import app.ch.currencyconverter.core.lifecycle.LoadingState

class LoadingIdlingResource(private val loadingState: LoadingState) : IdlingResource {

    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return loadingState::class.simpleName.orEmpty()
    }

    override fun isIdleNow(): Boolean {
        Thread.sleep(500)
        val isIdle = loadingState.isLoading.value != true
        if (isIdle) {
            callback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}
