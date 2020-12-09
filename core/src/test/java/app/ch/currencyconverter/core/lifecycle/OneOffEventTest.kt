package app.ch.currencyconverter.core.lifecycle

import io.mockk.*
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.*

class OneOffEventTest {

    @Test
    fun isConsumed_initial() {
        val event = OneOffEvent("Testing")
        expectThat(event.isConsumed).isFalse()
    }

    @Test
    fun isConsumed_consumed() {
        val event = OneOffEvent("Testing")
        event.getDataIfNotConsumed()
        expectThat(event.isConsumed).isTrue()
    }

    @Test
    fun getDataIfNotConsumed_notConsumed() {
        val event = OneOffEvent("Testing")
        expectThat(event.getDataIfNotConsumed()).isEqualTo("Testing")
    }

    @Test
    fun getDataIfNotConsumed_consumed() {
        val event = OneOffEvent("Testing")
        event.getDataIfNotConsumed()
        expectThat(event.getDataIfNotConsumed()).isNull()
    }

    @Test
    fun runIfNotConsumed() {
        val slot = slot<String>()
        val block = mockk<(String) -> Unit>()
        every { block.invoke(capture(slot)) } just Runs
        val event = OneOffEvent("Testing")
        expectThat(event.isConsumed).isFalse()
        event.runIfNotConsumed(block)
        expectThat(slot.captured).isEqualTo("Testing")
        expectThat(event.isConsumed).isTrue()
    }

    @Test
    fun peek() {
        val event = OneOffEvent("Testing")
        expectThat(event.peek()).isEqualTo("Testing")
    }

    @Test
    fun testEquals_different() {
        val event1 = OneOffEvent("Testing")
        val event2 = OneOffEvent("testing")
        expectThat(event1 == event2).isFalse()
    }

    @Test
    fun testEquals_same() {
        val event1 = OneOffEvent("Testing")
        val event2 = OneOffEvent("Testing")
        expectThat(event1 == event2).isTrue()
    }

    @Test
    fun testHashCode_different() {
        val event1 = OneOffEvent("Testing")
        val event2 = OneOffEvent("testing")
        expectThat(event1.hashCode()).isNotEqualTo(event2.hashCode())
    }

    @Test
    fun testHashCode_same() {
        val event1 = OneOffEvent("Testing")
        val event2 = OneOffEvent("Testing")
        expectThat(event1.hashCode()).isEqualTo(event2.hashCode())
    }

    @Test
    fun testToString() {
        val event = OneOffEvent("Testing")
        expectThat(event.toString()).isEqualTo("Event(data=Testing, isConsumed=false)")
    }
}
