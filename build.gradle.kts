import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "mx.edu.uttt"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:3.12.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
    implementation("org.webjars.npm:vue:2.6.10")
    implementation("org.webjars.npm:vuetify:2.3.15")
    implementation("org.webjars.npm:mdi__font:5.0.45")
    implementation("org.webjars.npm:roboto-fontface:0.10.0")
    implementation("com.zaxxer:HikariCP:4.0.2")
    implementation("org.firebirdsql.jdbc:jaybird:4.0.2.java11")
    implementation("com.github.seratch:kotliquery:1.3.1")
    implementation("org.mindrot:jbcrypt:0.4") //cifra las contraseñas :(
    implementation("org.eclipse.jetty.http2:http2-server:9.4.29.v20200521")
    implementation("org.eclipse.jetty.alpn:alpn-api:1.1.3.v20160715")
    implementation("org.eclipse.jetty:jetty-alpn-conscrypt-server:9.4.29.v20200521")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
//toma todas las biblotecas de arrriba y lo que esta en el main
tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "MainKt")
    }
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}

application {
    mainClassName = "MainKt"
}
