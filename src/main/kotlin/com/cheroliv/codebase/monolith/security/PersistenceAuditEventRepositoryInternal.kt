package com.cheroliv.codebase.monolith.security

import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

interface PersistenceAuditEventRepositoryInternal {
    fun findByPrincipal(principal: String?): Flux<PersistentAuditEvent?>?
    fun findAllByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?, pageable: Pageable?): Flux<PersistentAuditEvent?>?
    fun findByAuditEventDateBefore(before: Instant?): Flux<PersistentAuditEvent?>?
    fun findAllBy(pageable: Pageable?): Flux<PersistentAuditEvent?>?
    fun countByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?): Mono<Long?>?
}