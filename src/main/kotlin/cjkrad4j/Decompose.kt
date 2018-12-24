package cjkrad4j

class Decompose {
    private val EXCLUSION: String = "⿰⿱⿸⿺⿳⿻⿵⿲⿹⿴⿷⿶"

    private val subRadical: MutableMap<Char, Set<Char>> = mutableMapOf()
    private val superRadical: MutableMap<Char, MutableSet<Char>> = mutableMapOf()

    init {
        this::class.java.classLoader.getResource("ids.txt").readText().trimEnd().split("\n")
                .filter { !it.startsWith("#") }
                .forEach { row -> run {
                    val contents: List<String> = row.split("\t")
                    subRadical[contents[1].toCharArray().get(0)] =
                            Regex("(\\[[^\\]]+\\]|&[^;]+;|[$EXCLUSION])").replace(contents[2], "").toCharArray().toSet()
                } }

        subRadical.forEach{(k, v) ->  run {
            v.forEach { c -> run {
                if(!superRadical.containsKey(c)) {
                    superRadical[c] = mutableSetOf(k)
                }
                superRadical[c]!!.add(k)
            } }
        } }
    }

    fun getSubCompositions(c: Char): Set<Char> {
        return subRadical[c]?.toSet() ?: setOf()
    }

    fun getSuperCompositions(c: Char): Set<Char> {
        return  superRadical[c]?.toSet() ?: setOf()
    }
}
