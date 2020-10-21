package com.cheroliv.agence.gateway.security

import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepositoryInternal : DeleteExtended<AuthUser?> {
    fun findOneWithAuthoritiesByLogin(login: String?): Mono<AuthUser?>?
    fun findOneWithAuthoritiesByEmailIgnoreCase(email: String?): Mono<AuthUser?>?
    fun findAllByLoginNot(pageable: Pageable?, login: String?): Flux<AuthUser?>?
}