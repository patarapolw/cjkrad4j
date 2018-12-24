package cjkrad4j

import cjkrad4j.container.RadicalObject

class RadicalFinder (language: String = "zh") {
    private val decompose = Decompose()
    private val variant = Variant()
    private val frequency = Frequency(language)

    operator fun get(c: Char) = RadicalObject(c, decompose, frequency, variant)
}