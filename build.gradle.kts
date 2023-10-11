plugins {
    kotlin("jvm").version(libs.versions.kotlin.asProvider().get())
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "se.yverling.lab.ktor"
version = "0.0.1"

application {
    mainClass.set("se.yverling.lab.ktor.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)

    testImplementation(libs.ktor.client.contentNegotiation)
    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
}
