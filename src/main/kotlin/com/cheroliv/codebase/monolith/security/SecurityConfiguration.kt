package com.cheroliv.codebase.monolith.security

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport

//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
@Import(SecurityProblemSupport::class)
class SecurityConfiguration {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(SecurityConfiguration::class.java)
    }
}