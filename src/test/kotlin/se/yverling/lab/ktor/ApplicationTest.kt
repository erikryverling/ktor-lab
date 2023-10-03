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
import kotlin.test.Test
import kotlin.test.assertIs

class ApplicationTest {
    @Test
    fun `should store and retrieve coffee successfully`() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val testCoffee = Coffee(name = "Guji Supreme", roaster = "Test roaster", origin = "Guji")

        // Post

        var response = client.post("/coffee") {
            contentType(ContentType.Application.Json)
            setBody(testCoffee)
        }

        assertEquals("Coffee stored correctly", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)

        // Get by id

        response = client.get("/coffee")

        val coffees = response.body<List<Coffee>>()
        assertEquals(HttpStatusCode.OK, response.status)
        assertIs<List<Coffee>>(coffees)
        assertEquals(coffees[0], testCoffee)
    }
}
