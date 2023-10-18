package se.yverling.lab.ktor

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import se.yverling.lab.ktor.models.Coffee
import se.yverling.lab.ktor.plugins.configureRouting
import se.yverling.lab.ktor.plugins.configureSerialization
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertIs

class ApplicationTest {
    @Test
    fun `should store and retrieve coffee successfully`() = testApplication {
        application {
            configureRouting("localhost", 6380)
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val id = UUID.randomUUID().toString()
        val testCoffee = Coffee(id = id, name = "Guji Supreme", roaster = "Test roaster", origin = "Guji")

        // Post

        var response = client.post("/coffee") {
            contentType(ContentType.Application.Json)
            setBody(testCoffee)
        }

        assertEquals("Coffee stored correctly", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)

        response = client.get("/coffee/$id")

        val coffee = response.body<Coffee>()
        assertEquals(HttpStatusCode.OK, response.status)
        assertIs<Coffee>(coffee)
        assertEquals(coffee, testCoffee)
    }
}
