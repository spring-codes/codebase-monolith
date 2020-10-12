package com.cheroliv.agence.gateway.config

import org.slf4j.LoggerFactory

import org.springframework.context.annotation.Import
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport

//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity


//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
@Import(SecurityProblemSupport::class)
class SecurityConfiguration {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(SecurityConfiguration::class.java)
    }
}