package kat.auth


import io.javalin.http.Context
import kotliquery.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

data class AccountUser(val id: String, val userId: String, val password: String, val role: Role,
                       val firstname: String, val lastname: String, val maidname: String, val activeaccount: Boolean  )


object AccountService {

    private val toAccountUser: (Row) -> AccountUser = { row ->
        AccountUser(
            row.string("ID"),
            row.string("USER_ID"),
            row.string("USER_PASSWORD"),
            when(row.string("USER_ROLE")) {
                "USER" -> Role.USER
                "ADMIN" -> Role.ADMIN
                else -> Role.UNAUTHENTICATED
            },
            row.string("USER_FIRSTNAME"),
            row.string("USER_LASTNAME"),
            row.string("USER_MAIDNAME"),
            row.boolean("ACTIVE_ACCOUNT")
        )
    }


    fun create(ctx: Context): String{
        val id = UUID.randomUUID().toString()
        val user = ctx.bodyValidator<AccountUser>().get()
        val query = queryOf("""
           INSERT INTO USUARIOS (ID, USER_ID, USER_PASSWORD, USER_ROLE, USER_FIRSTNAME,
            USER_LASTNAME, USER_MAIDNAME, ACTIVE_ACCOUNT)
            VALUES (CHAR_TO_UUID(?),?,?,?,?,?,?,?)""".trimMargin(),
            id, user.userId, BCrypt.hashpw(user.password, BCrypt.gensalt()), user.role.name, user.firstname, user.lastname, user.maidname, user.activeaccount
        )
        var result = "Failed"
        using(sessionOf(HikariCP.dataSource())) {
            result = if (it.run(query.asUpdate) > 0) id else "Failed"

        }
        return result
    }

    fun update(ctx: Context, id: String): String{
        val user = ctx.bodyValidator<AccountUser>().get()
        val query = queryOf("""
            UPDATE USUARIOS SET 
            USER_ID = ?, USER_PASSWORD = ?, USER_ROLE = ?, USER_FIRSTNAME = ?, 
            USER_LASTNAME = ?, USER_MAIDNAME = ?, ACTIVE_ACCOUNT = ?
            WHERE ID = CHAR_TO_UUID(?)""".trimIndent(),
            user.userId, BCrypt.hashpw(user.password,BCrypt.gensalt()), user.role.name,
            user.firstname, user.lastname, user.maidname, user.activeaccount, id
        )
        var result = "Failed"
        using(sessionOf(HikariCP.dataSource())) {
            result = if (it.run(query.asUpdate) > 0) "Success" else "Failed"
        }

        return result
    }

    fun getALL(): List<AccountUser>{
        var user = listOf<AccountUser>()
        val query = queryOf("""
          SELECT UUID_TO_CHAR(ID) AS ID, USER_ID, USER_PASSWORD, USER_ROLE, USER_FIRSTNAME,
          USER_LASTNAME, USER_MAIDNAME, ACTIVE_ACCOUNT
          FROM USUARIOS""".trimIndent())
            .map{ AccountService.toAccountUser(it) }.asList
        using(sessionOf(HikariCP.dataSource())){
            user = it.run(query)
        }
        return user
    }

    fun findById(id: String): AccountUser? {

        val query = queryOf("""
         SELECT UUID_TO_CHAR(ID) AS ID, USER_ID, USER_PASSWORD, USER_ROLE, USER_FIRSTNAME,
         USER_LASTNAME, USER_MAIDNAME, ACTIVE_ACCOUNT
         FROM USUARIOS WHERE ID = CHAR_TO_UUID(?)""".trimIndent(), id)
            .map { toAccountUser(it) }.asSingle
        var result: AccountUser? = null
        using(sessionOf(HikariCP.dataSource())) {
            result = it.run(query)

        }
        return  result
    }

    fun findByI2(id: String): AccountUser?{
        val query = queryOf("""
         SELECT UUID_TO_CHAR(ID) AS ID, USER_ID, USER_PASSWORD, USER_ROLE, USER_FIRSTNAME,
         USER_LASTNAME, USER_MAIDNAME, ACTIVE_ACCOUNT
         FROM USUARIOS WHERE USER_ID = ?""".trimIndent(), id)
            .map { toAccountUser(it) }.asSingle
        var result: AccountUser? = null
        using(sessionOf(HikariCP.dataSource())) {
            result = it.run(query)

        }
        return  result
    }

    fun findByIdAndPassword(id: String, password: String): AccountUser? {
        val user = findByI2(id) ?: return null
        return if (BCrypt.checkpw(password, user.password ) && user.activeaccount) user else null
    }

}
