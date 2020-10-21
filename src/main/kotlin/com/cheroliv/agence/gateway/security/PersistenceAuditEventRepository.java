package com.cheroliv.agence.gateway.security;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the {@link PersistentAuditEvent} entity.
 */
@Repository
public interface PersistenceAuditEventRepository extends R2dbcRepository<PersistentAuditEvent, Long>, PersistenceAuditEventRepositoryInternal {

    @Query("INSERT INTO persistent_audit_evt_data VALUES(:eventId, :name, :value)")
    Mono<Void> savePersistenceAuditEventData(Long eventId, String name, String value);
}

