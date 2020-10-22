package com.cheroliv.agence.gateway.security

import com.cheroliv.agence.gateway.security.PersistentAuditEvent
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
import java.time.ZoneOffset

class PersistenceAuditEventRepositoryInternalImpl(private val databaseClient: DatabaseClient) : PersistenceAuditEventRepositoryInternal {
    override fun findByPrincipal(principal: String?): Flux<PersistentAuditEvent?>? {
        return findAllByCriteria(Criteria.where("principal").`is`(principal!!))
    }

    override fun findAllByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?, pageable: Pageable?): Flux<PersistentAuditEvent?>? {
        // LocalDateTime seems to be the only type that is supported across all drivers atm
        // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
        val fromDateLocal = LocalDateTime.ofInstant(fromDate, ZoneOffset.UTC)
        val toDateLocal = LocalDateTime.ofInstant(toDate, ZoneOffset.UTC)
        val criteria = Criteria
                .where("event_date").greaterThan(fromDateLocal)
                .and("event_date").lessThan(toDateLocal)
        return findAllFromSpec(select().matching(criteria).page(pageable!!))
    }

    override fun findByAuditEventDateBefore(before: Instant?): Flux<PersistentAuditEvent?>? {
        // LocalDateTime seems to be the only type that is supported across all drivers atm
        // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
        val beforeLocal = LocalDateTime.ofInstant(before, ZoneOffset.UTC)
        return findAllByCriteria(Criteria.where("event_date").lessThan(beforeLocal))
    }

    override fun findAllBy(pageable: Pageable?): Flux<PersistentAuditEvent?>? {
        return findAllFromSpec(select().page(pageable!!))
    }

    override fun countByAuditEventDateBetween(fromDate: Instant?, toDate: Instant?): Mono<Long?>? {
        // LocalDateTime seems to be the only type that is supported across all drivers atm
        // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
        val fromDateLocal = LocalDateTime.ofInstant(fromDate, ZoneOffset.UTC)
        val toDateLocal = LocalDateTime.ofInstant(toDate, ZoneOffset.UTC)
        return databaseClient.execute("SELECT COUNT(DISTINCT event_id) FROM persistent_audit_event " +
                "WHERE event_date > :fromDate AND event_date < :toDate")
                .bind("fromDate", fromDateLocal)
                .bind("toDate", toDateLocal)
                .`as`(Long::class.java)
                .fetch()
                .one()
    }

    private fun findAllByCriteria(criteria: Criteria): Flux<PersistentAuditEvent?> {
        return findAllFromSpec(select().matching(criteria))
    }

    private fun select(): TypedSelectSpec<PersistentAuditEvent> {
        return databaseClient.select().from(PersistentAuditEvent::class.java)
    }

    private fun findAllFromSpec(spec: TypedSelectSpec<PersistentAuditEvent>): Flux<PersistentAuditEvent?> {
        return spec.`as`(PersistentAuditEvent::class.java).all()
                .flatMap { event: PersistentAuditEvent ->
                    findAllEventData(event.id)
                            .map { data: Map<String?, String?>? ->
                                event.data = requireNotNull(data as Map<String, String>)
                                event
                            }
                }
    }

    private fun findAllEventData(id: Long?): Mono<Map<String?, String?>> {
        return databaseClient.select().from("persistent_audit_evt_data")
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
}