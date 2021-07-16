package kat.auth


import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.util.RateLimit
import java.util.concurrent.TimeUnit

data class Credentials(val userId: String, val password: String)

object AuthController {

    fun signIn(ctx: Context) { //inicia sesion
        RateLimit(ctx).requestPerTimeUnit(20, TimeUnit.HOURS)
        val (userId, password) = ctx.bodyValidator<Credentials>()
            .check({ it.userId.isNotBlank() && it.password.isNotBlank() }, "Ambos campos son obligatorios")
            .get()
        val user = AccountService.findByIdAndPassword(userId, password) ?: throw BadRequestResponse("Usuario desactivado")
        ctx.userInfo = UserInfo( user.id, user.userId, user.role, "${user.firstname} ${user.lastname}" )
        ctx.result("success")
    }

    fun signOut(ctx: Context) {//cierra la sesion
        ctx.userInfo = null
    }

}
