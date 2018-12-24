package cjkrad4j.container

import cjkrad4j.Decompose
import cjkrad4j.Frequency
import cjkrad4j.Variant

class RadicalObject internal constructor (
        val c: Char,
        private val decompose: Decompose,
        private val frequency: Frequency,
        private val variant: Variant) {

    val subCompositions: List<Char>
        get() = decompose.getSubCompositions(c).toList()

    val superCompositions: List<Char>
        get() = decompose.getSuperCompositions(c).sortedBy { frequency[it] }

    val variants: List<Char>
        get() = variant[c].toList()

    fun flatten(): RadicalDataClass = RadicalDataClass(subCompositions, superCompositions, variants)

    fun equals(other: RadicalObject): Boolean {
        return flatten() == other.flatten()
    }
}