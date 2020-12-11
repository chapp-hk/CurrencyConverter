package app.ch.currencyconverter.mockserver

import app.ch.currencyconverter.FileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SimpleSuccessDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse().setResponseCode(200).setBody(
            when {
                request.path.orEmpty().contains("/list") -> FileReader.get("list.json")
                request.path.orEmpty().contains("/live") -> FileReader.get("live.json")
                else -> FileReader.get("error.json")
            }
        )
    }
}

class FailureDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse().setBody(FileReader.get("error.json"))
    }
}
