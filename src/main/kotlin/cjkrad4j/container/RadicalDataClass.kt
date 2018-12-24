package cjkrad4j.container

data class RadicalDataClass internal constructor (
        val subCompositions: List<Char>,
        val superCompositions: List<Char>,
        val variants: List<Char>
)