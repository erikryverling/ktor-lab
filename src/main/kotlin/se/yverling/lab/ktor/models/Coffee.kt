package se.yverling.lab.ktor.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Coffee(val id: String = UUID.randomUUID().toString(), val name: String, val roaster: String, val origin: String)

val coffeeStorage = mutableListOf<Coffee>()
