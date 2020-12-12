package app.ch.currencyconverter.mockserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockWebServerRule : TestWatcher() {

    private val mockWebServer = MockWebServer()

    override fun starting(description: Description?) {
        super.starting(description)
        mockWebServer.start(8080)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        mockWebServer.shutdown()
    }

    fun mockSuccess(dispatcher: Dispatcher = SimpleSuccessDispatcher()) {
        mockWebServer.dispatcher = dispatcher
    }

    fun mockFailure() {
        mockWebServer.dispatcher = FailureDispatcher()
    }

    fun mockCustomResponse(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }
}
