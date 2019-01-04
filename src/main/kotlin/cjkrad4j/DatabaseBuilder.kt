package cjkrad4j

import java.io.File
import java.sql.DriverManager

class DatabaseBuilder (
        databaseUrl: String,
        private val idsTxtPath: String,
        private val jundaTxtPath: String,
        private val unihanVariantPath: String
) {
    private val conn = DriverManager.getConnection(databaseUrl)

    init {
        conn.autoCommit = false
        val statement = conn.createStatement()

        statement.execute("""
            CREATE TABLE character (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                character   TEXT NOT NULL UNIQUE,
                frequency   INT
            );
        """.trimIndent())

        statement.execute("""
            CREATE TABLE character_sub (
                character_id    INT REFERENCES character(id),
                subRadical_id   INT REFERENCES character(id),
                PRIMARY KEY (character_id, subRadical_id)
            );
        """.trimIndent())

        statement.execute("""
            CREATE TABLE character_super (
                character_id    INT REFERENCES character(id),
                superRadical_id   INT REFERENCES character(id),
                PRIMARY KEY (character_id, superRadical_id)
            );
        """.trimIndent())

        statement.execute("""
            CREATE TABLE character_variant (
                character_id    INT REFERENCES character(id),
                variant_id   INT REFERENCES character(id),
                PRIMARY KEY (character_id, variant_id)
            );
        """.trimIndent())
    }

    fun build() {
        val exclusion = "⿰⿱⿸⿺⿳⿻⿵⿲⿹⿴⿷⿶"

        File(idsTxtPath).readLines()
                .filter { !it.startsWith("#") }
                .forEach { row -> run {
                    val contents: List<String> = row.split('\t')
                    val c: String = contents[1][0].toString()

                    if (Regex("\\p{IsHan}").find(c) != null) {
                        val subRadical: Set<String> = Regex("(\\[[^\\]]+\\]|&[^;]+;|[$exclusion])")
                                .replace(contents[2], "")
                                .toCharArray().map { it.toString() }.toSet()

                        setOf(c, *subRadical.toTypedArray()).forEach {
                            val ps = conn.prepareStatement("""
                            INSERT INTO character (character)
                            VALUES (?)
                            ON CONFLICT (character)
                            DO NOTHING;
                        """.trimIndent())
                            ps.setString(1, it)
                            ps.execute()
                        }

                        subRadical.forEach {
                            var ps = conn.prepareStatement("""
                            INSERT INTO character_sub (character_id, subRadical_id)
                            SELECT c.id, s.id
                            FROM character AS c, character AS s
                            WHERE c.character = ? AND s.character = ?
                            ON CONFLICT
                            DO NOTHING;
                        """.trimIndent())
                            ps.setString(1, c)
                            ps.setString(2, it)
                            ps.execute()

                            ps = conn.prepareStatement("""
                            INSERT INTO character_super (character_id, superRadical_id)
                            SELECT c.id, s.id
                            FROM character AS c, character AS s
                            WHERE c.character = ? AND s.character = ?
                            ON CONFLICT
                            DO NOTHING;
                        """.trimIndent())
                            ps.setString(1, it)
                            ps.setString(2, c)
                            ps.execute()
                        }
                    }
                } }

        conn.commit()

        File(jundaTxtPath).readLines()
                .map { it.split('\t')[1][0].toString() }.forEachIndexed { index, c -> run {
                    val ps = conn.prepareStatement("""
                INSERT INTO character (character, frequency)
                VALUES (?, ?)
                ON CONFLICT (character)
                DO UPDATE SET frequency = ?
            """.trimIndent())
                    ps.setString(1, c)
                    ps.setInt(2, index)
                    ps.setInt(3, index)
                    ps.execute()
                } }

        conn.commit()

        File(unihanVariantPath).readLines().forEach {
            Regex("U\\+([0-9A-F]{4,})\t(\\w+)\t(.+)").find(it)?.let { it2 -> run {
                val (_, cPoint, _, variants) = it2.groupValues
                val c = cPoint.toInt(16).toChar().toString()
                val vSet = Regex("U\\+([0-9A-F]{4,})").findAll(variants).map { v ->
                    v.groupValues[1].toInt(16).toChar().toString()
                }

                setOf(c, *vSet.toList().toTypedArray()).forEach { it2 ->
                    val ps = conn.prepareStatement("""
                            INSERT INTO character (character)
                            VALUES (?)
                            ON CONFLICT (character)
                            DO NOTHING;
                        """.trimIndent())
                    ps.setString(1, it2)
                    ps.execute()
                }

                vSet.forEach { it2 ->
                    val ps = conn.prepareStatement("""
                            INSERT INTO character_variant (character_id, variant_id)
                            SELECT c.id, s.id
                            FROM character AS c, character AS s
                            WHERE c.character = ? AND s.character = ?
                            ON CONFLICT
                            DO NOTHING;
                        """.trimIndent())
                    ps.setString(1, c)
                    ps.setString(2, it2)
                    ps.execute()
                }
            } }
        }

        conn.commit()
        conn.close()
    }

    fun clean() {
        conn.createStatement().execute("""
            DELETE FROM character_sub
            WHERE
                character_id = (SELECT id FROM character WHERE character = '?') OR
                subRadical_id = (SELECT id FROM character WHERE character = '?');
            DELETE FROM character_super
            WHERE
                character_id = (SELECT id FROM character WHERE character = '?') OR
                superRadical_id = (SELECT id FROM character WHERE character = '?');
            DELETE FROM character WHERE character = '?';
        """.trimIndent())
        conn.commit()
    }
}

fun main(args : Array<String>) {
    DatabaseBuilder(
            databaseUrl = "jdbc:sqlite:cjkrad4j.db",
            idsTxtPath = "/Users/patarapolw/GitHubProjects/cjkrad4j/src/main/resources/ids.txt",
            jundaTxtPath = "/Users/patarapolw/GitHubProjects/cjkrad4j/src/main/resources/junda.tsv",
            unihanVariantPath = "/Users/patarapolw/GitHubProjects/cjkrad4j/src/main/resources/Unihan_Variants.txt"
    ).build()
}