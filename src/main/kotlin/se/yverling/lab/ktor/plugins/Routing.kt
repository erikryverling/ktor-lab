package se.yverling.lab.ktor.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import se.yverling.lab.ktor.routes.coffeeRouting

fun Application.configureRouting(dbHost: String, dbPort: Int) {
    routing {
        coffeeRouting(dbHost, dbPort)
    }
}
