package kat.auth

import io.javalin.http.Context
import io.javalin.http.Handler
import java.io.Serializable
import io.javalin.core.security.Role as JavalinRole

enum class Role : JavalinRole {
    UNAUTHENTICATED,
    USER,
    ADMIN
}

object AccessManager {
    fun manage(handler: Handler, ctx: Context, permittedRoles: Set<JavalinRole>) {
        ctx.refreshUserInfo() // make sure role matches database role
        when {
            Role.UNAUTHENTICATED in permittedRoles -> handler.handle(ctx)
            ctx.userInfo == null -> ctx.redirect("/login")
            ctx.userInfo!!.role in permittedRoles -> handler.handle(ctx) // user role fits any onf the roles for the endpoint
            else -> ctx.status(401)
        }
    }
}

private fun Context.refreshUserInfo() {
    this.userInfo?.let {
        val user=AccountService.findById(it.id)!!
        this.userInfo = UserInfo(it.id, user.userId, user.role, "${user.firstname} ${user.lastname}") }
}

data class UserInfo(val id: String, val userId: String, val role: Role, val fullName: String) : Serializable // must be serializable to store in session file/db

var Context.userInfo: UserInfo?
    get() = this.sessionAttribute("USER_INFO")
    set(userInfo) = this.sessionAttribute("USER_INFO", userInfo)

