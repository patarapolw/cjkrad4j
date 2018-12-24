package cjkrad4j

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class VariantTest {
    private val variant = Variant()

    @TestFactory
    fun testGet() = listOf(
            '语' to setOf('語')
    ).map { (input, output)->
        dynamicTest("Variants of $input is $output") {
            assertEquals(variant[input], output)
        }
    }
}