package com.cheroliv.agence.gateway

import com.cheroliv.agence.gateway.GatewayAgenceApp.Companion.log
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class GatewayAgenceApp {
    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(GatewayAgenceApp::class.java)
    }
    val greeting: String = "Hello world."
}

fun main(args: Array<String>) {
    log.info("Hi...")
    runApplication<GatewayAgenceApp>(*args)
}
