package com.cheroliv.agence.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class GatewayAgenceApp {
    val greeting: String = "Hello world."
}

fun main(args: Array<String>) {
    runApplication<GatewayAgenceApp>(*args)
}
