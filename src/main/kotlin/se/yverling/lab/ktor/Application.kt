package se.yverling.lab.ktor

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import se.yverling.lab.ktor.plugins.configureRouting
import se.yverling.lab.ktor.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())

    val dbHost = config.property("ktor.db.host").getString()
    val dbPort = config.property("ktor.db.port").getString().toInt()

    configureRouting(dbHost, dbPort)
    configureSerialization()
}
