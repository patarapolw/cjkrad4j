package cjkrad4j

import com.google.gson.Gson
import java.lang.Exception

class Frequency constructor(language: String = "zh") {
    private var order: Map<Char, Int> = mapOf();

    init {
        when(language) {
            "zh", "cn" -> parseJunda()
            "jp", "ja" -> parseGrade()
            else -> throw Exception("Language $language not supported")
        }
    }

    private fun parseJunda() {
        order = this::class.java.classLoader.getResource("junda.tsv").readText().trimEnd().split("\n")
                .map { it.split("\t")[1][0] }.mapIndexed { index, c -> c to index  }.toMap();
    }

    private fun parseGrade() {
        val gson = Gson()
        order = gson.fromJson(
                this::class.java.classLoader.getResource("grade.json").readText(),
                HashMap<String, String>()::class.java
        ).values.joinToString("").toCharArray().reversed().mapIndexed { index, c -> c to index }.toMap();
    }

    operator fun get(c: Char): Int? = order[c]
}