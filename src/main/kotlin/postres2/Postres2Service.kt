package postres2

import io.javalin.http.Context
import kotliquery.*
import java.util.*

data class Postre(
    var id: String? = null,
    val name: String? = null,
    val fat: Double? = null,
    val carbs: Double? = null,
    val proteins: Double? = null,
    val calories: Double? = null)

object Postres2Service {
    fun insert(ctx: Context) : String {
        val id = UUID.randomUUID().toString()
        val postre = ctx.bodyValidator<Postre>().get()
        postre.id = id
        println("Postre: $postre")
        val qry = queryOf("""
            INSERT INTO POSTRES (ID, NAME, FAT, CARBS, PROTEIN, CALORIES)
            VALUES (CHAR_TO_UUID(?), ?, ?, ?, ?, ?)""".trimIndent(),
            id, postre.name, postre.fat, postre.carbs, postre.proteins, postre.calories)
        var result = "failed"
        using(sessionOf(HikariCP.dataSource())) {
            result = if ( it.run( qry.asUpdate ) > 0 ) id else "failed"
        }
        return result
    }

    private val toPostre : (Row) -> Postre = { row ->
        Postre(
            row.string("ID"),
            row.string("NAME"),
            row.double("FAT"),
            row.double("CARBS"),
            row.double("PROTEIN"),
            row.double("CALORIES")
        )
    }

    fun selectedAll(ctx: Context) : List<Postre> {
        var postres = listOf<Postre>()
        var qry = queryOf("""
             SELECT UUID_TO_CHAR(ID) AS ID, NAME, FAT, CARBS, PROTEIN, CALORIES
             FROM POSTRES""".trimIndent())
            .map { toPostre(it) }.asList
        using(sessionOf(HikariCP.dataSource())){
            postres = it.run(qry)
        }
        return postres
    }

    fun selectById(resourceId: String): Postre {
        val qry = queryOf("""
            SELECT UUID_TO_CHAR(ID) AS ID, NAME, FAT, CARBS, PROTEIN, CALORIES
            FROM POSTRES WHERE ID = CHAR_TO_UUID(?)
        """.trimIndent(), resourceId)
            .map(toPostre).asSingle
        var result = Postre()
        using(sessionOf(HikariCP.dataSource())) {
            result = it.run(qry) ?: Postre()
        }
        return result
    }

    fun DeleteById( ctx: Context, resourceId: String ): String {
        val id = resourceId.toString()
        val qryDel = queryOf("""
            DELETE FROM POSTRES WHERE ID = CHAR_TO_UUID(?)
            """.trimIndent(), id)
        var result = "failed"
        using(sessionOf(HikariCP.dataSource())){
            result = if ( it.run( qryDel.asUpdate ) > 0 ) "SUCCESS" else "failed"
        }
        return result
    }

    fun UpdateById( ctx: Context, resourceId: String ) : String{
        val postre = ctx.bodyValidator<Postre>().get()
        val id = resourceId.toString()
        println("Postre: $postre")

        val qry = queryOf("""
            UPDATE POSTRES SET NAME = ?, FAT = ?, CARBS = ?, PROTEIN = ?, CALORIES = ?
            WHERE ID = CHAR_TO_UUID(?)""".trimIndent(),
            postre.name, postre.fat, postre.carbs, postre.proteins, postre.calories, id)
        var result = "failed"
        using(sessionOf(HikariCP.dataSource())){
            result = if ( it.run( qry.asUpdate ) > 0 ) "SUCCESS" else "failed"
        }
        return result
    }

}