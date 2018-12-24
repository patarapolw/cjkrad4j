package cjkrad4j.container
import cjkrad4j.RadicalFinder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class RadicalObjectTest {
    val radicalFinder = RadicalFinder()

    @TestFactory
    fun testSubCompositions() = listOf(
            '你' to listOf('亻', '尔')
    ).map { (input, output)->
        DynamicTest.dynamicTest("RadicalFinder of $input shows subCompositions of $output") {
            Assertions.assertEquals(radicalFinder[input].subCompositions, output)
        }
    }

    @TestFactory
    fun testSuperCompositions() = listOf(
            '你' to listOf('您')
    ).map { (input, output)->
        DynamicTest.dynamicTest("RadicalFinder of $input shows superCompositions of $output") {
            Assertions.assertEquals(radicalFinder[input].superCompositions, output)
        }
    }

    @TestFactory
    fun testVariants() = listOf(
            '语' to listOf('語')
    ).map { (input, output)->
        DynamicTest.dynamicTest("RadicalFinder of $input shows variants of $output") {
            Assertions.assertEquals(radicalFinder[input].variants, output)
        }
    }

    @TestFactory
    fun testFlatten() = listOf(
            '语' to listOf('語')
    ).map { (input, output)->
        DynamicTest.dynamicTest("RadicalFinder of $input shows variants of $output") {
            Assertions.assertEquals(radicalFinder[input].variants, output)
        }
    }
}