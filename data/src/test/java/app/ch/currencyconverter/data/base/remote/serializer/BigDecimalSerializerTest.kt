package app.ch.currencyconverter.data.base.remote.serializer

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class BigDecimalSerializerTest {

    @MockK
    private lateinit var decoder: Decoder

    @MockK
    private lateinit var encoder: Encoder

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun deserialize() {
        every { decoder.decodeDouble() } returns 30.0

        expectThat(BigDecimalSerializer.deserialize(decoder)).isEqualTo(30.0.toBigDecimal())
    }

    @Test
    fun serialize() {
        every { encoder.encodeDouble(any()) } just runs

        BigDecimalSerializer.serialize(encoder, 678.0.toBigDecimal())

        verify(exactly = 1) { encoder.encodeDouble(678.0) }
    }
}
