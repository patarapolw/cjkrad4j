package cjkrad4j

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class DecomposeTest {
    private val decompose = Decompose()

    @TestFactory
    fun testGetSub() = listOf(
            '你' to setOf('亻', '尔')
    ).map { (input, output)->
        dynamicTest("Subcompositions of $input are $output") {
            assertEquals(decompose.getSubCompositions(input), output)
        }
    }

    @TestFactory
    fun testGetSuper() = listOf(
            '你' to setOf('您')
    ).map { (input, output)->
        dynamicTest("Supercompositions of $input are $output") {
            assertEquals(decompose.getSuperCompositions(input), output)
        }
    }
}
