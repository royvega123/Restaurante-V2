import auth.Session
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.rendering.vue.VueComponent
import kat.auth.AuthController
import kat.auth.Role
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.plugin.rendering.vue.JavalinVue
import kat.auth.AccessManager
import kat.auth.userInfo
import kotliquery.HikariCP
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory
import postres.PostresController
import postres2.Postres2Controller

fun main() {
    HikariCP.default("jdbc:firebirdsql://localhost:3050/Postres?encoding=UTF8","sysdba", "1234")
    val app = Javalin.create { config ->
        config.enableWebjars()
        config.addStaticFiles("/public")
        config.server{ createHttp2Server()}
        config.sessionHandler { Session.sqlSessionHandler() }
        config.accessManager(AccessManager::manage) //
        JavalinVue.stateFunction = { ctx -> mapOf("userInfo" to ctx.userInfo) }
        JavalinVue.optimizeDependencies = true

    }.start()
    //
    app.routes {
        get("/", VueComponent("Inicio"), roles(Role.UNAUTHENTICATED))
        get("/Registro", VueComponent("Registro"), roles(Role.UNAUTHENTICATED))
        get("/home-page", VueComponent("home-page"), roles(Role.USER, Role.ADMIN) )
        get("/crud", VueComponent("crud"), roles(Role.USER, Role.ADMIN))
        get("/login", VueComponent("sign-in-page"), roles(Role.UNAUTHENTICATED))
        get("/account", VueComponent("account-page"), roles(Role.ADMIN))
        get("/examples-page", VueComponent("examples-page"), roles(Role.UNAUTHENTICATED))
    }
    //
    app.routes {
        path("/api") {
            path("/auth") {
                post("/sign-in", AuthController::signIn, roles(Role.UNAUTHENTICATED))
                post("/sing-out", AuthController::signOut, roles(Role.UNAUTHENTICATED))
                crud("/account/:id", AccountController(), roles(Role.UNAUTHENTICATED))
            }
            crud("/postres2/:id", Postres2Controller(), roles(Role.USER, Role.ADMIN))

            path("/postres"){
                get(PostresController::getAllPostres)
                post(PostresController::createPostre)
                path(":id") {
                    get(PostresController::getPostre)
                    put(PostresController::updatePostre)
                    delete(PostresController::deletePostre)
                }
            }
        }

    }

}

private fun createHttp2Server(): Server {

    val alpn = ALPNServerConnectionFactory().apply {
        defaultProtocol = "h2"
    }

    val sslContextFactory = SslContextFactory().apply {
        keyStorePath = this::class.java.getResource("/keystore").toExternalForm() // reemplácelo con su real keystore
        setKeyStorePassword("123456") // reemplácelo con su real password
        cipherComparator = HTTP2Cipher.COMPARATOR
        provider = "Conscrypt"
    }

    val ssl = SslConnectionFactory(sslContextFactory, alpn.protocol)

    val httpsConfig = HttpConfiguration().apply {
        sendServerVersion = false
        secureScheme = "https"
        securePort = 8443
        addCustomizer(SecureRequestCustomizer())
    }

    val http2 = HTTP2ServerConnectionFactory(httpsConfig)

    val fallback = HttpConnectionFactory(httpsConfig)

    return Server().apply {
        //HTTP/1.1 Connector
        addConnector(ServerConnector(server).apply {
            port = 8080
        })
        // HTTP/2 Connector
        addConnector(ServerConnector(server, ssl, alpn, http2, fallback).apply {
            port = 8443
        })
    }

}