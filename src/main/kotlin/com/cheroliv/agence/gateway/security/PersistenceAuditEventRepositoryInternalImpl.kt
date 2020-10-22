package com.cheroliv.agence.gateway.security

import io.r2dbc.spi.Row
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.DatabaseClient.TypedSelectSpec
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.util.Pair
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

class PersistenceAuditEventRepositoryInternalImpl(private val databaseClient: DatabaseClient)
    : PersistenceAuditEventRepositoryInternal {
    override fun findByPrincipal(principal: String?): Flux<PersistentAuditEvent?>? =
            findAllByCriteria(Criteria.where("principal").`is`(principal!!))

    // LocalDateTime seems to be the only type that is supported across all drivers atm
    // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
    override fun findAllByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?, pageable: Pageable?): Flux<PersistentAuditEvent?>? =
            findAllFromSpec(select().matching(Criteria
                    .where("event_date").greaterThan(LocalDateTime.ofInstant(fromDate, UTC))
                    .and("event_date").lessThan(LocalDateTime.ofInstant(toDate, UTC))).page(pageable!!))

    // LocalDateTime seems to be the only type that is supported across all drivers atm
    // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
    override fun findByAuditEventDateBefore(before: Instant?): Flux<PersistentAuditEvent?>? =
            findAllByCriteria(Criteria.where("event_date").lessThan(LocalDateTime.ofInstant(before, UTC)))

    override fun findAllBy(pageable: Pageable?): Flux<PersistentAuditEvent?>? =
            findAllFromSpec(select().page(pageable!!))

    // LocalDateTime seems to be the only type that is supported across all drivers atm
    // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
    override fun countByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?): Mono<Long?>? =
            databaseClient.execute("SELECT COUNT(DISTINCT event_id) FROM persistent_audit_event " +
                    "WHERE event_date > :fromDate AND event_date < :toDate")
                    .bind("fromDate", LocalDateTime.ofInstant(fromDate, UTC))
                    .bind("toDate", LocalDateTime.ofInstant(toDate, UTC))
                    .`as`(Long::class.java)
                    .fetch()
                    .one()

    private fun findAllByCriteria(criteria: Criteria): Flux<PersistentAuditEvent?> =
            findAllFromSpec(select().matching(criteria))

    private fun select(): TypedSelectSpec<PersistentAuditEvent> =
            databaseClient.select().from(PersistentAuditEvent::class.java)

    private fun findAllFromSpec(spec: TypedSelectSpec<PersistentAuditEvent>): Flux<PersistentAuditEvent?> =
            spec.`as`(PersistentAuditEvent::class.java).all()
                    .flatMap { event: PersistentAuditEvent ->
                        findAllEventData(event.id)
                                .map { data: Map<String?, String?>? ->
                                    event.data = requireNotNull(data as Map<String, String>)
                                    event
                                }
                    }

    private fun findAllEventData(id: Long?): Mono<Map<String?, String?>> =
            databaseClient.select().from("persistent_audit_evt_data")
                    .project("name", "value")
                    .matching(Criteria.where("event_id").`is`(id!!))
                    .map { row: Row ->
                        val name = row.get("name", String::class.java)
                        val value = row.get("value", String::class.java)
                        Pair.of(name ?: "", value ?: "")
                    }
                    .all()
                    .collectMap({ obj: Pair<String, String> -> obj.first }) { obj: Pair<String, String> -> obj.second }
}