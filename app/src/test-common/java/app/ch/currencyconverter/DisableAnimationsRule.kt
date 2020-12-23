package app.ch.currencyconverter

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DisableAnimationsRule : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        disableAnimations()
    }

    override fun finished(description: Description?) {
        super.finished(description)
        enableAnimations()
    }

    private fun disableAnimations() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand("settings put global transition_animation_scale 0")
            executeShellCommand("settings put global window_animation_scale 0")
            executeShellCommand("settings put global animator_duration_scale 0")
        }
    }

    private fun enableAnimations() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand("settings put global transition_animation_scale 1")
            executeShellCommand("settings put global window_animation_scale 1")
            executeShellCommand("settings put global animator_duration_scale 1")
        }
    }
}
