package cjkrad4j

import java.sql.DriverManager

class RadicalFinder {
    private val conn = DriverManager.getConnection("jdbc:sqlite::resource:cjkrad4j.db")
    data class RadicalData (
            val subCompositions: List<String>,
            val superCompositions: List<String>,
            val variants: List<String>
    )

    operator fun get(c: String): RadicalData {
        return RadicalData(
                subCompositions = getComponent(c, "subRadical"),
                superCompositions = getComponent(c, "superRadical"),
                variants = getComponent(c, "variant")
        )
    }

    private fun getComponent(c: String, part: String): List<String> {
        val result = mutableListOf<String>()

        val tableName = when (part) {
            "subRadical" -> "character_sub"
            "superRadical" -> "character_super"
            "variant" -> "character_variant"
            else -> throw Exception()
        }

        val ps = conn.prepareStatement("""
            SELECT s.character
            FROM character AS c
            INNER JOIN $tableName AS cs ON cs.character_id = c.id
            INNER JOIN character AS s ON cs.${part}_id = s.id
            WHERE c.character = ?
            ORDER BY s.frequency ASC
        """.trimIndent())
        ps.setString(1, c[0].toString())
        val rs = ps.executeQuery()

        while (rs.next()) {
            result.add(rs.getString(1))
        }

        return result.toList()
    }
}