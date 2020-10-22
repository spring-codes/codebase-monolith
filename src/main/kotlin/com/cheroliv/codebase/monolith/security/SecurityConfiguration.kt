package com.cheroliv.codebase.monolith.security

import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport

//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport::class)
class SecurityConfiguration(
        val tokenProvider: TokenProvider) {
    companion object {
        @JvmStatic
        private val log by lazy { getLogger(SecurityConfiguration::class.java) }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()

    private fun securityConfigurerAdapter(): JWTConfigurer? = JWTConfigurer(tokenProvider)

}