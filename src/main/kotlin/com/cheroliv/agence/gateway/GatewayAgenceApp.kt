package com.cheroliv.agence.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayAgenceApp {
    val greeting: String = "Hello world."
}

fun main(args: Array<String>) {
    runApplication<GatewayAgenceApp>(*args)
}
