package auth

import org.eclipse.jetty.server.session.*
import java.io.File

object Session {
    // Puede obtener más información sobre el manejo de sesiones aquí: https://javalin.io/tutorials/jetty-session-handling

    // la sesión se almacena en un archivo; esto funciona bien en localhost, pero considere cambiar si está implementando su aplicación
    fun fileSessionHandler() = SessionHandler().apply {
        httpOnly = true
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                val baseDir = File(System.getProperty("java.io.tmpdir"))
                this.storeDir = File(baseDir, "kat-session-store").apply { mkdir() }
            }
        }
    }

    // use this if you need a session database
    fun sqlSessionHandler() = SessionHandler().apply {
        sessionCache = DefaultSessionCache(this).apply { // 
            sessionDataStore = JDBCSessionDataStoreFactory().apply {
                setDatabaseAdaptor(DatabaseAdaptor().apply {
                    setDriverInfo("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql://localhost:3050/Postres?encoding=UTF8&user=sysdba&password=1234")
                    })
            }.getSessionDataStore(sessionHandler)
        }
        httpOnly = true
        // make additional changes to your SessionHandler here
    }
}