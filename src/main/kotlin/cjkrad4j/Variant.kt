package cjkrad4j

class Variant {
    private var entries: Map<Char, Set<Char>>

    init {
        entries = this::class.java.classLoader.getResource("Unihan_Variants.txt").readText().split("\n")
                .map { it1 ->
                    Regex("U\\+([0-9A-F]{4,})\t(\\w+)\t(.+)").find(it1)?.let { it3 -> {
                        val (_, c, _, variants) = it3.groupValues
                        c.toInt(16).toChar() to Regex("U\\+([0-9A-F]{4,})").findAll(variants).map { v ->
                            v.groupValues[1].toInt(16).toChar()
                    }.toSet() } }?.invoke() }.filterNotNull().toMap()
    }

    operator fun get(c: Char): Set<Char> = entries[c] ?: setOf()
}