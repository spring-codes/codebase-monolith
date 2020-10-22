package com.cheroliv.agence.gateway.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import javax.annotation.PostConstruct

@Configuration
class WebConfigurer : WebFluxConfigurer {
    companion object {
        @JvmStatic
        private val log: Logger by lazy { getLogger(WebConfigurer::class.java) }
    }

    @PostConstruct
    fun foo() {
        log.info("webConfigurer")
    }
}