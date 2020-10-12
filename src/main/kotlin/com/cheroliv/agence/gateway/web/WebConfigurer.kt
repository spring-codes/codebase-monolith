package com.cheroliv.agence.gateway.web

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import javax.annotation.PostConstruct

@Configuration
class WebConfigurer : WebFluxConfigurer {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(WebConfigurer::class.java)
    }

    @PostConstruct
    fun foo() {
        log.info("webConfigurer")
    }
}