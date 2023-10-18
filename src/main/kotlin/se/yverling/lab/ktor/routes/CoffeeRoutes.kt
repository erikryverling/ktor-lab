package se.yverling.lab.ktor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import redis.clients.jedis.JedisPooled
import redis.clients.jedis.search.FTCreateParams
import redis.clients.jedis.search.IndexDataType
import redis.clients.jedis.search.Query
import redis.clients.jedis.search.schemafields.TextField
import se.yverling.lab.ktor.models.Coffee
import se.yverling.lab.ktor.models.toCoffee

private const val INDEX = "idx:coffees"
private const val PREFIX = "coffee"


fun Route.coffeeRouting(dbHost: String, dbPort: Int) {
    val jedis = JedisPooled(dbHost, dbPort)

    jedis.ftDropIndex(INDEX)

    jedis.ftCreate(
        "idx:coffees",
        FTCreateParams.createParams()
            .on(IndexDataType.JSON)
            .addPrefix(PREFIX),
        TextField.of("$.name").`as`("name"),
        TextField.of("$.roaster").`as`("roaster"),
        TextField.of("$.origin").`as`("origin")
    )

    route("/coffee") {
        get {
            val query = Query("*")
            val documents = jedis.ftSearch(INDEX, query).documents

            val coffees = documents.map {
                it.toCoffee()
            }

            call.respond(coffees)
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val coffee = jedis.jsonGet("$PREFIX:$id", Coffee::class.java)
                ?: return@get call.respondText(
                    "No coffee with id $id",
                    status = HttpStatusCode.NotFound
                )

            call.respond(coffee)
        }

        post {
            val coffee = call.receive<Coffee>()
            jedis.jsonSetWithEscape("$PREFIX:${coffee.id}", coffee)

            call.respondText("Coffee stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            jedis.del("$PREFIX:$id")
            call.respondText("Coffee removed correctly", status = HttpStatusCode.Accepted)
        }
    }
}