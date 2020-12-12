package app.ch.currencyconverter.mockserver

import java.io.InputStreamReader

object FileReader {
    fun get(fileName: String): String {
        return InputStreamReader(javaClass.classLoader?.getResourceAsStream(fileName))
            .use { it.readText() }
    }
}
