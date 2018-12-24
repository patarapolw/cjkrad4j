package cjkrad4j

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class FrequencyTest {
    private val frequency = Frequency()

    @TestFactory
    fun testGet() = listOf(
            '你' to 31
    ).map { (input, output) ->
        dynamicTest("Frequency of $input character is $output") {
            assertEquals(frequency[input], output)
        }
    }
}