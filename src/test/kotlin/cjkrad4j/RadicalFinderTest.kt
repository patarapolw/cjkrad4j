package cjkrad4j

import cjkrad4j.container.RadicalDataClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class RadicalFinderTest {
    private val radicalFinder = RadicalFinder()

    @TestFactory
    fun testRadicalFinder() = listOf(
            '你' to RadicalDataClass(
                    subCompositions = listOf('亻', '尔'),
                    superCompositions = listOf('您'),
                    variants = listOf('妳')
            )
    ).map { (input, output)->
        DynamicTest.dynamicTest("RadicalFinder of $input shows $output") {
            Assertions.assertEquals(radicalFinder[input].flatten(), output)
        }
    }
}