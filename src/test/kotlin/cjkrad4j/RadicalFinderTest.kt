package cjkrad4j

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class RadicalFinderTest {
    private val radicalFinder = RadicalFinder("jdbc:sqlite:cjkrad4j.db")

    @TestFactory
    fun testRadicalFinder() = listOf(
            "你",
            "好",
            "你好"
    ).map { input ->
        DynamicTest.dynamicTest("RadicalFinder of $input") {
            println(radicalFinder[input])
        }
    }
}