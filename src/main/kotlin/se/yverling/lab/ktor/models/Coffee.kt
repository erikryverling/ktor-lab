package se.yverling.lab.ktor.models

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import redis.clients.jedis.search.Document
import java.util.*

@Serializable
data class Coffee(val id: String = UUID.randomUUID().toString(), val name: String, val roaster: String, val origin: String)

fun Document.toCoffee(): Coffee {
    return Gson().fromJson(this.properties.first().value.toString(), Coffee::class.java)
}
