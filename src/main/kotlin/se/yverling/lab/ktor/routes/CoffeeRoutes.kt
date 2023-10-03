package se.yverling.lab.ktor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import se.yverling.lab.ktor.models.Coffee
import se.yverling.lab.ktor.models.coffeeStorage

fun Route.coffeeRouting() {
    route("/coffee") {
        get {
            call.respond(coffeeStorage)
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                coffeeStorage.find { it.id == id } ?: return@get call.respondText(
                    "No coffee with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }

        post {
            val customer = call.receive<Coffee>()
            coffeeStorage.add(customer)
            call.respondText("Coffee stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (coffeeStorage.removeIf { it.id == id }) {
                call.respondText("Coffee removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
